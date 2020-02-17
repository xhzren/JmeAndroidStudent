package cn.xhzren.game.mutil.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class HelloMessage extends AbstractMessage {

    private String content;

    public HelloMessage() {
    }
    public HelloMessage(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HelloMessage{" +
                "content='" + content + '\'' +
                '}';
    }
}
