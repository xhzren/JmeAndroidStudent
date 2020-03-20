package cn.xhzren.netty.websocket;

import cn.xhzren.netty.servers.RedisHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DramaMessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered" + ctx.channel().id());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered" + ctx.channel().id());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame text = (TextWebSocketFrame)msg;
            System.out.println("收到消息" + text.text());
            ctx.writeAndFlush(new TextWebSocketFrame("server 现在时间 = " + LocalDateTime.now()));
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(ctx.channel().id() +"is active");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Jedis jedis = RedisHelper.getJedis();
        jedis.set(ctx.channel().id().asShortText(), ctx.channel().id().asShortText());
        RedisHelper.close(jedis);
//        System.out.println(ctx.channel().id() + "-> 加入了server");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(ctx.channel().id() + "-> 离去了server");
        Jedis jedis = RedisHelper.getJedis();
        jedis.del(ctx.channel().id().asShortText());
        RedisHelper.close(jedis);
    }

}
