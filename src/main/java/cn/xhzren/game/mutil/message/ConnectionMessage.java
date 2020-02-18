package cn.xhzren.game.mutil.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class ConnectionMessage extends AbstractMessage {

    private String id;
    private String account;
    private String password;
    private String version;
    private String ip;

    public ConnectionMessage() {
    }

}
