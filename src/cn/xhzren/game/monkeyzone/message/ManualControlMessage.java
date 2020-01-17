package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Manual (human) control message, used bidirectional
 * 手动（人工）控制器消息，双向使用
 * @author normenhansen
 */
@Serializable()
public class ManualControlMessage extends PhysicsSyncMessage {

    public float aimX;
    public float aimY;
    public float moveX;
    public float moveY;
    public float moveZ;

    public ManualControlMessage() {
    }

    public ManualControlMessage(ManualControlMessage msg) {
        this.aimX = msg.aimX;
        this.aimY = msg.aimY;
        this.moveX = msg.moveX;
        this.moveY = msg.moveY;
        this.moveZ = msg.moveZ;
    }

    public ManualControlMessage(long id, float aimX, float aimY, float moveX, float moveY, float moveZ) {
        this.syncId = id;
        this.aimX = aimX;
        this.aimY = aimY;
        this.moveX = moveX;
        this.moveY = moveY;
        this.moveZ = moveZ;
    }

    @Override
    public void appData(Object object) {

    }
}
