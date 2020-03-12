package cn.xhzren.netty.client;

import cn.xhzren.netty.SimpleMain;
import cn.xhzren.netty.appstates.StartAppState;
import cn.xhzren.netty.appstates.TransitionSceneAppState;
import cn.xhzren.netty.entity.LocalAccountData;
import cn.xhzren.netty.servers.handlers.FileReceiveServerHandler;
import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.*;
import cn.xhzren.netty.util.JsonUtils;
import cn.xhzren.netty.util.MessageBuild;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DetectVersionClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(DetectVersionClientHandler.class);

    private SimpleMain app;
    private ChannelHandlerContext ctx;

    public DetectVersionClientHandler(SimpleMain app) {
        this.app = app;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
            if(msg.getDataType() == DataType.UpdateAsset){
                logger.info("需要update assets: {}", msg.getUpdateAsset());
                ConnectionMessage message = ConnectionMessage.
                        newBuilder().setDataType(DataType.RequestInfo)
                        .setRequestInfo(RequestInfo.newBuilder()
                                .setRequestType(RequestInfo.RequestType.UPDATE_ASSESTS)
                                .build()).build();
                ctx.writeAndFlush(message);
                ctx.pipeline().addAfter(this.getClass().getName(), FileReceiveServerHandler.class.getName(),
                        new FileReceiveClientHandler());
            }else if(msg.getReceiveInfo().getReceiveType() == ReceiveType.VERSION_RECEIVE &&
                msg.getReceiveInfo().getReceiveStatus() == ReceiveStatus.NONE) {
                //not update
                //find active token
                Optional<LocalAccountData> activeToken = JsonUtils.localData.stream().filter((e)->
                        e.isActive()
                ).filter((e)->
                        !e.getToken().isEmpty()
                ).findFirst();
                if(activeToken.isPresent()) {
                    ConnectionMessage message = MessageBuild.requestInfoBuild(RequestInfo.RequestType.VERIFY_TOKEN).build();
                    ctx.writeAndFlush(message);
                    ctx.pipeline().remove(this);
                }else {
                    //发起login请求
                    app.getStateManager().getState(StartAppState.class).isClose = true;
                    app.getStateManager().attach(new TransitionSceneAppState());
                }
            }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    public void sendMsg(ConnectionMessage msg) {
        ctx.writeAndFlush(msg);
    }
}
