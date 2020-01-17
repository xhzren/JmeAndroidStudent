package cn.xhzren.game.monkeyzone.network.physicssync;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Abstract physics sync message, can be used with PhysicsSyncManager, contains
 * timestamp and id. Override applyData method to apply the data to the object
 * with the specific id when it arrives.
 * @author normenhansen
 * 可以与PhysicsSyncManager一起使用的抽象物理消息同步, 包含时间戳和id.
 * 重写appData方法, 在数据到达时, 将数据应用于具有特定Id的对象.
 */
@Serializable
public abstract class PhysicsSyncMessage extends AbstractMessage {

    public long syncId = -1;
    public double time;

    public PhysicsSyncMessage() {
        super(true);
    }

    public PhysicsSyncMessage(long id) {
        super(true);
        this.syncId = id;
    }

    public abstract void appData(Object object);
}
