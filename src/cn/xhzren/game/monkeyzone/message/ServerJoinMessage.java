package cn.xhzren.game.monkeyzone.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
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
