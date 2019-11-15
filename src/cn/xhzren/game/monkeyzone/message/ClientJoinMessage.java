package cn.xhzren.game.monkeyzone.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class ClientJoinMessage extends AbstractMessage {

    public String name;
    public String pass;

    public ClientJoinMessage() {}


    public ClientJoinMessage(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }
}
