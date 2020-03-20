package cn.xhzren.netty.websocket;

import cn.xhzren.netty.appstates.ClientAppState;
import cn.xhzren.netty.servers.RedisHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import redis.clients.jedis.Jedis;

public class ShakeHandsHandler extends SimpleChannelInboundHandler<Object> {

    Jedis jedis;

    public ShakeHandsHandler() {
        jedis = RedisHelper.getJedis();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest)msg;
            //如果不成功或者消息头不包含"Upgrade"，说明不是websocket连接，报400异常
            if(request.decoderResult().isSuccess() ||
                    (!"websocket".equals(request.headers().get("Upgrade")))) {
                sendHttpResponse(ctx,request,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.INTERNAL_SERVER_ERROR));
            }else if(RedisHelper.getJedis().get(request.headers().get("Authentication")) == null) {
                sendHttpResponse(ctx,request,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION));
            }
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        if(response.status().code() != HttpResponseStatus.OK.code()) {
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
        }
        //发送message
        ChannelFuture f = ctx.channel().writeAndFlush(response);
    }

}
