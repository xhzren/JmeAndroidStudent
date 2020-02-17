package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.network.serializing.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * Message sent when entering entity
 * 当进入实体时发送消息
 * @author normenhansen
 */
@Serializable()
@ToString
public class ServerEnterEntityMessage extends PhysicsSyncMessage {

    public long playerId;
    public long entityId;

    public ServerEnterEntityMessage() {
    }

    public ServerEnterEntityMessage(long playerId, long entityId) {
        syncId = -1;
        this.entityId = entityId;
        this.playerId = playerId;
    }

    @Override
    public void appData(Object object) {
        WorldManager worldManager = (WorldManager)object;
        worldManager.enterEntity(playerId, entityId);
    }
}
