package cn.xhzren.game.monkeyzone.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 基础聊天消息
 */
@Serializable
public class ChatMessage extends AbstractMessage {

    private String text;
    private String name;

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
