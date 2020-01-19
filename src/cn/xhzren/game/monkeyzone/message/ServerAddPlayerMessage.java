package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.network.serializing.Serializable;
import lombok.ToString;

/**
 * used by the server to add a player on the client
 * 服务器用来在客户端上添加玩家
 * @author normenhansen
 */
@Serializable()
@ToString
public class ServerAddPlayerMessage extends PhysicsSyncMessage {

    public long playerId;
    public String name;
    public int group_id;
    public int ai_id;

    public ServerAddPlayerMessage() {
    }

    public ServerAddPlayerMessage(long id, String name, int group_id, int ai_id) {
        this.syncId = -1;
        this.playerId = id;
        this.name = name;
        this.group_id = group_id;
        this.ai_id = ai_id;
    }

    @Override
    public void appData(Object object) {
        WorldManager manager = (WorldManager) object;
        manager.addPlayer(playerId, group_id, name, ai_id);
    }
}
