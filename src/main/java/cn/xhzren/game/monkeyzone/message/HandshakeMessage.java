package cn.xhzren.game.monkeyzone.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.ToString;

/**
 * used for first handshake. contains protocol, client and server version.
 * should never change.
 * 用于第一次握手。 包含协议，客户端和服务器版本。永远不要更改。
 * @author normenhansen
 */
@Serializable
@ToString
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
