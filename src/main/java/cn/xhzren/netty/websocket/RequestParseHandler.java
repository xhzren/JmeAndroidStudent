package cn.xhzren.netty.websocket;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.List;

public class RequestParseHandler extends ChannelInboundHandlerAdapter {
    List<Room> active = new ArrayList<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame text = (TextWebSocketFrame)msg;
            Room room = JSONObject.parseObject(text.text(), Room.class);
            if(room != null) {
                active.add(room);
            }
        }
        super.channelRead(ctx, msg);
    }
}
