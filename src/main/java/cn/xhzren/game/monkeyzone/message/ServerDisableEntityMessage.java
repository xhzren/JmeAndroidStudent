package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.network.serializing.Serializable;
import lombok.ToString;

/**
 * used by the server to add an entity on the client
 * 服务器用来在服务器上禁用实体
 * @author normenhansen
 */
@Serializable
@ToString
public class ServerDisableEntityMessage extends PhysicsSyncMessage {

    public long entityId;

    public ServerDisableEntityMessage() {
    }

    public ServerDisableEntityMessage(long entity_id) {
        this.syncId = -1;
        this.entityId = entity_id;
    }

    @Override
    public void appData(Object object) {
        WorldManager worldManager = (WorldManager)object;
        worldManager.disableEntity(entityId);
    }
}
