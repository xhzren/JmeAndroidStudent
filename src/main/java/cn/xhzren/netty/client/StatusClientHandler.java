package cn.xhzren.netty.client;

import cn.xhzren.netty.SimpleMain;
import cn.xhzren.netty.appstates.StartAppState;
import cn.xhzren.netty.appstates.TransitionSceneAppState;
import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(StatusClientHandler.class);

    private SimpleMain app;

    public StatusClientHandler(SimpleMain app) {
        this.app = app;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
        if(msg.getDataType() == DataType.ReceiveInfo) {
            ReceiveStatus msgStatus = msg.getReceiveInfo().getReceiveStatus();
            ReceiveType msgReceiveType = msg.getReceiveInfo().getReceiveType();

            logger.warn("receiveInfo : {}",msg.getReceiveInfo());
//            if(msgStatus == ReceiveStatus.ERROR) {
//                if(msgReceiveType == ReceiveType.LOGIN_RECEIVE) {
//                }else if(msgReceiveType == ReceiveType.TOKEN_RECEIVE) {
//                }else if(msgReceiveType == ReceiveType.FILE_RECEIVE) {
//                }
//                ctx.channel().close();
//            }else
                if(msgStatus == ReceiveStatus.FAIL) {
                if(msgReceiveType == ReceiveType.TOKEN_RECEIVE) {
                    logger.info(msg.getReceiveInfo().getContent());
                    //发起login请求
                    app.getStateManager().getState(StartAppState.class).isClose = true;
                    app.getStateManager().attach(new TransitionSceneAppState());
                }
            }else {
                ctx.fireChannelRead(msg);
            }
        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
