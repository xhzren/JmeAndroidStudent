package cn.xhzren.game.monkeyzone.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * sent to client to signal that if it has logged in, contains human players id
 * 发送给客户端以指示其已登录，其中包含玩家id
 * @author normenhansen
 */
@Serializable
@ToString
public class ServerJoinMessage extends AbstractMessage {

    public boolean rejected;
    public long id;
    public int group_id;
    public String name;

    public ServerJoinMessage() {

    }

    public ServerJoinMessage(boolean rejected, long id, int group_id, String name) {
        this.rejected = rejected;
        this.id = id;
        this.group_id = group_id;
        this.name = name;
    }
}
