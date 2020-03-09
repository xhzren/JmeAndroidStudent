package cn.xhzren.netty.client;

import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(StatusClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
        if(msg.getDataType() == DataType.ReceiveInfo) {
            ReceiveStatus msgStatus = msg.getReceiveInfo().getReceiveStatus();
            ReceiveType msgReceiveType = msg.getReceiveInfo().getReceiveType();
            if(msgStatus == ReceiveStatus.ERROR) {
                logger.error(msg.getReceiveInfo().getContent());
                if(msgReceiveType == ReceiveType.LOGIN_RECEIVE) {
                }else if(msgReceiveType == ReceiveType.TOKEN_RECEIVE) {
                }else if(msgReceiveType == ReceiveType.FILE_RECEIVE) {
                }
                ctx.channel().close();
            }else if(msgStatus == ReceiveStatus.SUCCESS || msgStatus == ReceiveStatus.CONTINUE) {
                ctx.fireChannelRead(msg);
            }else if(msgStatus == ReceiveStatus.FAIL) {
                if(msgReceiveType == ReceiveType.TOKEN_RECEIVE) {
                    logger.info(msg.getReceiveInfo().getContent());
                    //发起登录请求
                    ConnectionMessage login = ConnectionMessage.newBuilder()
                            .setDataType(ConnectionMessage.DataType.LoginType)
                            .setLogin(Login.newBuilder().setId(1).setName("test")
                                    .setLoginType(Login.LoginType.SELF)
                                    .setPassWord("123455").build()).build();
                    ctx.writeAndFlush(login);
                }
            }
        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
