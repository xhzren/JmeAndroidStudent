package cn.xhzren.game.monkeyzone.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.ToString;

/**
 * sent from client to join server
 * 从客户端发送加入服务器的消息
 * @author normenhansen
 */
@Serializable
@ToString
public class ClientJoinMessage extends AbstractMessage {

    public String name;
    public String pass;

    public ClientJoinMessage() {}


    public ClientJoinMessage(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }
}
