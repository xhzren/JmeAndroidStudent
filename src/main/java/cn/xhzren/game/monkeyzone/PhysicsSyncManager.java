package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.message.SyncCharacterMessage;
import cn.xhzren.game.monkeyzone.message.SyncRigidBodyMessage;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import cn.xhzren.game.monkeyzone.network.physicssync.SyncMessageValidator;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.network.*;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhysicsSyncManager extends AbstractAppState implements MessageListener {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    Application app;
    private Server server;
    private Client client;
    //同步频率
    private float syncFrequency = 0.25f;
    //
    LinkedList<SyncMessageValidator> validators = new LinkedList<>();
    //同步对象管理器
    HashMap<Long, Object> syncObjects = new HashMap<>();
    double time = 0;
    double offset = Double.MIN_VALUE;
    //最大延迟
    private double maxDelay = 0.50f;
    //同步计时器
    float syncTimer = 0;

    //消息队列
    LinkedList<PhysicsSyncMessage> messagesQueue = new LinkedList<>();


    public PhysicsSyncManager(Server server, Application app) {
        this.server = server;
        this.app = app;
    }

    public PhysicsSyncManager(Client client, Application app) {
        this.client = client;
        this.app = app;
    }

    @Override
    public void update(float tpf) {
        //时间增加
        time += tpf;
        if(time<0) {
            //TODO 溢出
            //重置时间
            time = 0;
        }

        if(client != null) {
            for (Iterator<PhysicsSyncMessage> it = messagesQueue.iterator();it.hasNext();) {
                //如果客户端不为空, 获取同步消息队列
                PhysicsSyncMessage message = it.next();
                if(message.time >= time+offset) {
                    //如果这个消息对象中的时间戳大于 (正常时间+偏移量), 说明数据错误
                    //对Id发送同步数据
                    sendSyncData();
                    //重置同步计时器
                    syncTimer = 0;
                }
            }
        }
    }

    /**
     * add an object to the list of objects managed by this sync manager.
     * 将对象添加到此对象管理器管理的对象列表中.
     * @param id
     * @param object
     */
    public void addObject(long id, Object object) {
        syncObjects.put(id, object);
    }

    /**
     * removes an object from the list of objects managed by this sync manager.
     * 从此同步管理器管理的对象列表中删除一个对象.
     * @param object
     */
    public void removeObject(Object object) {
        for (Iterator<Map.Entry<Long,Object>> it = syncObjects.entrySet().iterator();it.hasNext();) {
            Map.Entry<Long,Object> entry = it.next();
            if(entry.getValue() == object) {
                it.remove();
                return;
            }
        }
    }

    /**
     * removes an object from the list of objects managed by this sync manager.
     * 从此同步管理器管理的对象列表中删除一个对象.
     * @param id
     */
    public void removeObject(long id) {
        syncObjects.remove(id);
    }

    public void clearObjects() {
        syncObjects.clear();
    }

    /**
     * executes a message immediately.
     * 立即执行一条消息.
     */
    protected void doMessage(PhysicsSyncMessage message) {
        //根据id获取同步对象
        Object object = syncObjects.get(message.syncId);
        if(object!= null) {
            //如果对象不为空, 调用该消息对象的应用数据方法
            message.appData(object);
        }else {
            LOGGER.log(Level.WARNING,
                    "Cannot find physics object for: ({0}){1}",
                    new Object[]{message.syncId, message});
        }
    }

    /**
     * enqueues the message and updates the offset of the sync manager based on the time stamp.
     * 让消息入列, 并根据时间戳来更新同步管理器的偏移量(offset)
     * @param message
     */
    protected void enqueueMessage(PhysicsSyncMessage message) {
        if(offset == Double.MIN_VALUE) {
            //如果第一次进来, offset一定等于Double.MIN_VALUE
            //初始化offset为 当前时间戳 - 消息对象时间戳
            offset = this.time - message.time;
            LOGGER.log(Level.INFO, "Initial offset {0}", offset);
        }

        //(消息对象时间戳+偏移时间) - 管理器时间戳 ,得到延迟时间
        double delayTime = (message.time + offset)-time;
        if(delayTime > maxDelay) {
            //如果延迟时间> 设置的最大延迟时间
            //得到offset为 offset-(延迟时间-最大延迟时间)
            offset -= delayTime - maxDelay;
            LOGGER.log(Level.INFO, "Decrease offset due to high delayTime ({0})", delayTime);
        }else if(delayTime < 0) {
            //如果延迟时间为负数
            //得到offset 为 offset - 延迟时间
            offset -= delayTime;
            LOGGER.log(Level.INFO, "Increase offset due the low delayTime ({0})", delayTime);
        }
        messagesQueue.add(message);
    }

    /**
     * sends sync data for all active physics objects.
     * 发送所有活动物理对象的同步数据
     */
    protected void sendSyncData() {
        for (Iterator<Map.Entry<Long, Object>> it = syncObjects.entrySet().iterator();it.hasNext();) {
            Map.Entry<Long, Object> entry = it.next();
            //获取所有同步对象
            if(entry.getValue() instanceof Spatial) {
                Spatial spat = (Spatial)entry.getValue();
                //如果这个对象是个Spatial, 根据RigidBodyControl获取物理对象
                PhysicsRigidBody body = spat.getControl(RigidBodyControl.class);
                if(body == null) {
                    //如果这个同步对象没有RigidBodyControl, 则从VehicleControl中获取
                    body = spat.getControl(VehicleControl.class);
                }
                if(body != null && body.isActive()) {
                    //如果body是活跃的, 发送SyncRigidBodyMessage
                    SyncRigidBodyMessage msg = new SyncRigidBodyMessage(entry.getKey(), body);
                    broadcast(msg);
                    continue;
                }

                //如果对象拥有CharacterControl, 则进行角色的数据同步
                CharacterControl control = spat.getControl(CharacterControl.class);
                if(control != null) {
                    SyncCharacterMessage msg = new SyncCharacterMessage(entry.getKey(), control);
                    broadcast(msg);
                }
            }
        }
    }

    /**
     * use to broadcast physics control messages if server, applies timestamp to
     * PhysicsSyncMessage, call from OpenGL thread!
     * 用于在服务器上广播物理控制消息, 将时间戳应用于PhysicsSyncMessage, 从OpenGL线程调用.
     * @param msg
     */
    public void broadcast(PhysicsSyncMessage msg) {
        if(server == null) {
            LOGGER.log(Level.INFO, "Broadcast message on client {0}", msg);
            return;
        }
        msg.time = time;
        server.broadcast(msg);
    }

    /**
     * send data to a specific client.
     * 发送数据到特定的客户端.
     */
    public void send(int client, PhysicsSyncMessage msg) {
        if(server == null)  {
            LOGGER.log(Level.SEVERE, "Broadcasting message on client {0}", msg);
        }
        send(server.getConnection(client), msg);

    }

    /**
     * send data to a specific client.
     * 发送数据到特定的客户端.
     */
    public void send(HostedConnection client, PhysicsSyncMessage msg) {
        msg.time = time;
        if(client == null) {
            LOGGER.log(Level.SEVERE, "Client null when sending: {0}", msg);
        }
        client.send(msg);
    }

    /**
     * registers the types of messages this PhysicsSyncManager listens to.
     * 注册此PhysicsSyncManager侦听的消息的类型.
     */
    public void setMessageTypes(Class... classes) {
        if(server != null) {
            server.removeMessageListener(this);
            server.addMessageListener(this, classes);
        }else if(client != null) {
            client.removeMessageListener(this);
            client.addMessageListener(this, classes);
        }
    }
    @Override
    public void messageReceived(Object source, final Message message) {
        assert (message instanceof PhysicsSyncMessage);
        if(client != null) {
                app.enqueue(new Callable<Void>() {
                    @Override
                    public Void call() {
                        enqueueMessage((PhysicsSyncMessage)message);
                        return null;
                    }
                });
        }else if(server != null) {
            app.enqueue(new Callable<Void>() {
                    @Override
                    public Void call() {
                        for (Iterator<SyncMessageValidator> it = validators.iterator();it.hasNext();) {
                            SyncMessageValidator syncMessageValidator = it.next();
                            if(syncMessageValidator.checkMessage((PhysicsSyncMessage)message)) {
                                return null;
                            }
                        }

                        broadcast((PhysicsSyncMessage)message);
                        doMessage((PhysicsSyncMessage)message);
                        return null;
                    }
            });
        }
    }

    public void addMessageValidator(SyncMessageValidator validator) {
        validators.add(validator);
    }
    public void removeMessageValidator(SyncMessageValidator validator) {
        validators.remove(validator);
    }

    public float getSyncFrequency() {
        return syncFrequency;
    }

    public void setSyncFrequency(float syncFrequency) {
        this.syncFrequency = syncFrequency;
    }

    public double getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(double maxDelay) {
        this.maxDelay = maxDelay;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }
}
