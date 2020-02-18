package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.network.serializing.Serializable;
import lombok.ToString;

/**
 * used by the server to remove an entity on the client
 * 服务器用来在客户端上删除实体
 * @author normenhansen
 */
@Serializable()
@ToString
public class ServerRemoveEntityMessage extends PhysicsSyncMessage {
    public long entityId;

    public ServerRemoveEntityMessage() {
    }

    public ServerRemoveEntityMessage(long entityId) {
        syncId = -1;
        this.entityId = entityId;
    }

    @Override
    public void appData(Object object) {
        WorldManager worldManager = (WorldManager)object;
        worldManager.removeEntity(entityId);
    }
}
