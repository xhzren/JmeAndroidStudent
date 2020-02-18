package cn.xhzren.game.monkeyzone.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.ToString;

/**
 * 基础聊天消息
 */
@Serializable
@ToString
public class ChatMessage extends AbstractMessage {

    public String text;
    public String name;

    public ChatMessage() {
    }
    public ChatMessage(String text) {
        this.text = text;
    }

    public ChatMessage(String text, String name) {
        this.text = text;
        this.name = name;
    }
}
