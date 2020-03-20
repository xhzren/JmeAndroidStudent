package cn.xhzren.netty.websocket;

import cn.xhzren.netty.entity.LoginProto.ConnectionMessage;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.DataType;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.ReceiveStatus;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.ReceiveType;
import cn.xhzren.netty.servers.RedisHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TokenVerifyServerHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
        DataType dataType = msg.getDataType();
        if(dataType == DataType.LoginType || dataType == DataType.FileReceive ||
            dataType == DataType.UpdateAsset || dataType == DataType.DetectVersion) {
            ctx.fireChannelRead(msg);
        }else {
            String token = msg.getRequestInfo().getToken();
            if(RedisHelper.getJedis().get(token) != null) {
                ctx.fireChannelRead(msg);
            }else {
                ConnectionMessage message = ConnectionMessage.newBuilder()
                        .setDataType(DataType.ReceiveInfo)
                        .setReceiveInfo(ReceiveInfo.newBuilder()
                                .setReceiveStatus(ReceiveStatus.ERROR)
                                .setReceiveType(ReceiveType.LOGIN_RECEIVE)
                                .setContent("character info verify error!").build()).build();
                ctx.writeAndFlush(message);
            }
        }
    }
}
