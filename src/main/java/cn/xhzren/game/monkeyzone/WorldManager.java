package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.ai.TriggerControl;
import cn.xhzren.game.monkeyzone.control.*;
import cn.xhzren.game.monkeyzone.data.PlayerData;
import cn.xhzren.game.monkeyzone.message.*;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import cn.xhzren.game.monkeyzone.network.physicssync.SyncMessageValidator;
import cn.xhzren.game.monkeyzone.util.NavMesh;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import groovyjarjarantlr.collections.impl.Vector;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldManager extends AbstractAppState implements SyncMessageValidator {

    private Server server;
    private Client client;
    private long myPlayerId = -2;
    private long myGroupId = -2;
    private NavMesh navMesh = new NavMesh();
    private Node rootNode;
    private Node worldNode;
    private HashMap<Long, Spatial> entities = new HashMap<>();
    private int newId = 0;
    private Application app;
    private AssetManager assetManager;
    private PhysicsSpace space;
    private List<Control> userControls = new ArrayList<>();
    private PhysicsSyncManager syncManager;
    private UserCommandControl commandInterface;

    public WorldManager(Application app, Node rootNode) {
        this.app = app;
        this.rootNode = rootNode;
        this.assetManager = app.getAssetManager();
        this.space = app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
        this.server = app.getStateManager().getState(PhysicsSyncManager.class).getServer();
        syncManager = app.getStateManager().getState(PhysicsSyncManager.class);
    }
    public WorldManager(Application app, Node rootNode,UserCommandControl commandControl) {
        this.app = app;
        this.rootNode = rootNode;
        this.assetManager = app.getAssetManager();
        this.space = app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
        this.client = app.getStateManager().getState(PhysicsSyncManager.class).getClient();
        this.commandInterface = commandControl;
        //TODO: criss-crossing of references between ai and world manager not nice..
        this.commandInterface.setWorldManager(this);
        syncManager = app.getStateManager().getState(PhysicsSyncManager.class);
    }

    @Override
    public boolean checkMessage(PhysicsSyncMessage message) {
        return false;
    }

    public boolean isServer() {
        return server != null;
    }

    public PhysicsSpace getPhysicsSpace() {
        return space;
    }

    public long getMyPlayerId() {
        return myPlayerId;
    }

    public void setMyPlayerId(long myPlayerId) {
        this.myPlayerId = myPlayerId;
    }

    public PhysicsSyncManager getSyncManager() {
        return syncManager;
    }

    public long getMyGroupId() {
        return myGroupId;
    }

    public void setMyGroupId(long myGroupId) {
        this.myGroupId = myGroupId;
    }
    /**
     * get the world root node (not necessarily the application rootNode!)
     * 获取世界的根节点(不一定是rootNode)
     * @return
     */
    public Node getWorldRoot() {
        return worldNode;
    }

    /**
     * loads the specified level node
     * 加载指定级别的节点
     * @param name
     */
    public void loadLevel(String name) {
        worldNode = (Node) assetManager.loadModel(name);
    }
    /**
     * attaches the level node to the rootnode
     * 将指定级别的节点添加到rootNode
     */
    public void attachLevel() {
        space.addAll(worldNode);
        rootNode.attachChild(worldNode);
    }
    /**
     * gets the entity with the specified id
     * 获取具有指定id的实体
     * @param id
     * @return
     */
    public Spatial getEntity(long id) {
        return entities.get(id);
    }

    /**
     * gets the entity belonging to a PhysicsCollisionObject
     * 获取属于PhysicsCollisionObject的实体
     * @param object
     * @return
     */
    public Spatial getEntity(PhysicsCollisionObject object) {
        Object obj = object.getUserObject();
        if(obj instanceof Spatial) {
            Spatial spatial = (Spatial)obj;
            if(entities.containsValue(spatial)) {
                return spatial;
            }
        }
        return null;
    }

    /**
     * disables an entity so that it is not displayed
     * 禁用实体，使其不显示
     * @param id
     */
    public void disableEntity(long id) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Disable entity: {0}", id);
        if(isServer()) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Broadcast disable entity: {0}", id);
            syncManager.broadcast(new ServerDisableEntityMessage(id));
        }
        Spatial spatial = entities.get(id);
        spatial.removeFromParent();
        space.removeAll(spatial);
    }

    /**
     * reenables an entity after it has been disabled
     * 在禁用实体之后重新启用.
     * @param id
     * @param location
     * @param rotation
     */
    public void enableEntity(long id, Vector3f location, Quaternion rotation) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Enable entity: {0}", id);
        if(isServer()) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Broadcast enable entity: {0}", id);
            syncManager.broadcast(new ServerEnableEntityMessage(id, location, rotation));
        }

        Spatial tmp = entities.get(id);
        setEntityTranslation(tmp, location, rotation);
        worldNode.attachChild(tmp);
        space.addAll(tmp);
    }

    /**
     * add an entity (vehicle, immobile house etc), always related to a spatial
     * with specific userdata like hp, maxhp etc. (sends message if server)
     * 添加一个实体(车辆,不动房屋等), 该实体始终与具有特定用户数据(例如hp,maxHp)的Spatial相关.
     * (如果拥有Server对象， 则发送消息)
     * @param id
     * @param modelIdentifier
     * @param location
     * @param rotation
     */
    public void addEntity(long id, String modelIdentifier, Vector3f location, Quaternion rotation) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Adding entity: {0}", id);

        if(isServer()) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Broadcast adding entity: {0}", id);
            syncManager.broadcast(new ServerAddEntityMessage(id, modelIdentifier, location, rotation));
        }

        Node entityModel = (Node) assetManager.loadModel(modelIdentifier);
        setEntityTranslation(entityModel, location, rotation);
        if(entityModel.getControl(CharacterControl.class) != null) {
            entityModel.addControl(new CharacterAnimControl());
            entityModel.getControl(CharacterControl.class).setFallSpeed(55);
            entityModel.getControl(CharacterControl.class).setJumpSpeed(15);
        }

        entityModel.setUserData("player_id", -11);
        entityModel.setUserData("group_id", -1);
        entityModel.setUserData("entity_id", id);
        entities.put(id, entityModel);
        syncManager.addObject(id, entityModel);
        space.addAll(entityModel);
        worldNode.attachChild(entityModel);
    }


    public void removeEntity(long id) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "Remove entity: {0}", id);
        if(isServer()) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "Broadcast removing entity: {0}", id);
            syncManager.broadcast(new ServerRemoveEntityMessage(id));
        }
        syncManager.removeObject(id);
        Spatial spatial = entities.remove(id);
        if(spatial == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
                    "try removing entity thats not there: {0}", id);
            return;
        }
        Long playerId = spatial.getUserData("player_id");
        removeTransientControls(spatial);
        removeAIControls(spatial);
        if(playerId == myPlayerId) {
           removeUserControls(spatial);
        }
        if(playerId != -1) {
            PlayerData.setData(playerId,"entity_id", -1);
        }
        //TODO: removing from aiManager w/o checking if necessary
        if(!isServer()) {
            commandInterface.removePlayerEntity(playerId);
        }
        spatial.removeFromParent();
        space.removeAll(spatial);
    }

    /**
     *  adds a player (sends message if server)
     *  添加一个玩家, (如果拥有Server对象, 则发送消息)
     * @param id
     * @param groupId
     * @param name
     * @param aiId
     */
    public void addPlayer(long id, int groupId, String name, int aiId) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Adding player : {0}", id);
        if(isServer()) {
            syncManager.broadcast(new ServerAddPlayerMessage(id, name, groupId, aiId));
        }
        PlayerData playerData = null;
        playerData = new PlayerData(id, groupId, name, aiId);
        PlayerData.add(id, playerData);
    }

    /**
     * removes a player
     * 删除一个玩家
     * @param id
     */
    public void removePlayer(long id) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "Remove player : {0}", id);
        if(isServer()) {
            //TODO: remove other (AI) entities if this is a human client..
            //如果这是人控制的, 则删除其他(AI)实体
            syncManager.broadcast(new ServerRemovePlayerMessage(id));
            long entityId = PlayerData.getLongData(id, "entity_id");
            if(entityId != -1){
                enterEntity(id, -1);
            }
            long characterId = PlayerData.getLongData(id, "character_entity_id");
            removePlayer(characterId);
        }
        PlayerData.remove(id);
    }

    /**
     * adds a control to the list of controls that are added to the spatial
     * currently controlled by the user (chasecam, ui control etc.)
     * 将control添加到列表, 该control列表将添加到用户当前控制的Spatial中
     * @param control
     */
    public void addUserControl(Control control) {
        userControls.add(control);
    }

    /**
     * detaches the level and clears the cache
     * 分离节点并清除缓存
     */
    public void closeLevel() {
        for (Iterator<PlayerData> it = PlayerData.getPlayers().iterator();it.hasNext();) {
            PlayerData data = it.next();
            data.setData("entity_id", -1);
        }
        if(isServer()) {
            for (Iterator<PlayerData> it = PlayerData.getPlayers().iterator();it.hasNext();) {
                PlayerData data = it.next();
                removePlayer(data.getId());
            }
        }

        for (Iterator<Long> et = new LinkedList(entities.keySet()).iterator();et.hasNext();) {
            Long entry = et.next();
            syncManager.removeObject(entry);
        }
        syncManager.clearObjects();
        entities.clear();
        newId = 0;
        space.removeAll(worldNode);
        rootNode.detachChild(worldNode);
        assetManager.clearCache();
    }

    /**
     * sets the translation of an entity based on its type
     * 根据实体的类型设置实体的位置变换
     * @param entityModel
     * @param location
     * @param rotation
     */
    private void setEntityTranslation(Spatial entityModel, Vector3f location,Quaternion rotation) {
        if(entityModel.getControl(RigidBodyControl.class) != null) {
            entityModel.getControl(RigidBodyControl.class).setPhysicsLocation(location);
            entityModel.getControl(RigidBodyControl.class).setPhysicsRotation(rotation.toRotationMatrix());
        }else if(entityModel.getControl(CharacterControl.class) != null) {
            entityModel.getControl(CharacterControl.class).setPhysicsLocation(location);
            entityModel.getControl(CharacterControl.class).setViewDirection(rotation.mult(Vector3f.UNIT_Z));
        }else if(entityModel.getControl(VehicleControl.class) != null) {
            entityModel.getControl(VehicleControl.class).setPhysicsLocation(location);
            entityModel.getControl(VehicleControl.class).setPhysicsRotation(rotation.toRotationMatrix());
        }else {
            entityModel.setLocalTranslation(location);
            entityModel.setLocalRotation(rotation);
        }
    }

    public void enterEntity(long playerId, long entityId) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Player {0} entering entity {1}", new Object[]{playerId, entityId});
        if(isServer()) {
            syncManager.broadcast(new ServerEnterEntityMessage(playerId, entityId));
        }

        long curEntity = PlayerData.getLongData(playerId, "entity_id");
        long groupId = PlayerData.getLongData(playerId, "group_id");
        //重置当前实体
        if(curEntity != -1) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Player {0} exiting current entity {1}", new Object[]{playerId, entityId});
            Spatial curEntitySpat = getEntity(curEntity);
            curEntitySpat.setUserData("player_id", -11);
            curEntitySpat.setUserData("group_id", -1);
            removeTransientControls(curEntitySpat);
            removeAIControls(curEntitySpat);
            if(playerId == groupId) {
                removeUserControls(curEntitySpat);
            }
        }
        PlayerData.setData(playerId, "entity_id", entityId);
        //if we entered an entity, configure its controls, id -1 means enter no entity
        //如果我们进入一个实体，请配置其控件，id -1表示不进入实体
        if(entityId != -1) {
            Spatial spat = getEntity(entityId);
            spat.setUserData("player_id", playerId);
            spat.setUserData("group_id", groupId);
            if(PlayerData.isHuman(playerId)) {
                if(groupId == getMyGroupId()) {//仅适用于Client

                }
            }

        }
    }

    public void makeManualControl(long entityId, Client client) {
        Spatial spat = getEntity(entityId);
        if(spat.getControl(CharacterControl.class) != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Make manual character control for entity {0} ", entityId);
            if(client != null) {
                //add net sending for users own manual control
                //TODO: using group id as client id
                if(((Integer)spat.getUserData("group_id")) == myGroupId) {
                    spat.addControl(new ManualCharacterControl(client, entityId));
                }else {
                    spat.addControl(new ManualCharacterControl());
                }
            }else {
                spat.addControl(new ManualCharacterControl());
            }
        }else if(spat.getControl(VehicleControl.class) != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Make manual vehicle control for entity {0} ", entityId);
            if (client != null) {
                //TODO: using group id as client id
                if ((Integer) spat.getUserData("group_id") == myGroupId) {
                    spat.addControl(new ManualVehicleControl(client, entityId));
                } else {
                    spat.addControl(new ManualVehicleControl());
                }
            } else {
                spat.addControl(new ManualVehicleControl());
            }
        }
    }

    /**
     * removes all movement controls (ManualControl / AutonomousControl) from spatial
     * 从Spatial中删除所有 运动控件
     * @param spat
     */
    public void removeTransientControls(Spatial spat) {
        ManualControl manualControl = spat.getControl(ManualControl.class);
        if(manualControl != null) {
            spat.removeControl(manualControl);
        }
        AutonomousControl autonomousControl = spat.getControl(AutonomousControl.class);
        if(autonomousControl != null) {
            spat.removeControl(autonomousControl);
        }
    }

    /**
     * removes the user controls for human user to the spatial
     * 将使用中的控件从Spatial上移除
     */
    public void removeUserControls(Spatial spat) {
        for (Iterator<Control> it = userControls.iterator();it.hasNext();) {
            Control control = it.next();
            spat.removeControl(control);
        }
    }

    /**
     * removes the command queue and triggers for user controlled ai entities
     * 从户控制的AI实体中删除命令队列和触发器用
     */
    public void removeAIControls(Spatial spat) {
        CommandControl aiControl = spat.getControl(CommandControl.class);
        if(aiControl != null) {
            spat.removeControl(aiControl);
        }
        TriggerControl triggerControl = spat.getControl(TriggerControl.class);
        while(triggerControl != null) {
            spat.removeControl(triggerControl);
            triggerControl = spat.getControl(TriggerControl.class);
        }
    }
}
