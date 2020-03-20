package cn.xhzren.netty.websocket;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Data;

@Data
public class Room extends BaseEntity {

    public Room() {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    private ChannelGroup channels;
    private String id;
    private String host;
    private String plotId;
    private int capacity;
    private String status;
}
