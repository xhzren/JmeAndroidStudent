package cn.xhzren.game.monkeyzone.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 第一次握手消息,
 */
@Serializable
public class HandshakeMessage extends AbstractMessage {
    public int protocol_version;
    public int client_version;
    public int server_version;

    public HandshakeMessage() {
    }

    public HandshakeMessage(int protocol_version, int client_version, int server_version) {
        this.protocol_version = protocol_version;
        this.client_version = client_version;
        this.server_version = server_version;
    }
}
