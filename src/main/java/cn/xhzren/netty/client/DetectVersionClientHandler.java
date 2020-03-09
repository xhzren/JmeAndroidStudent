package cn.xhzren.netty.client;

import cn.xhzren.netty.servers.handlers.FileReceiveServerHandler;
import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.util.JsonUtils;
import cn.xhzren.netty.util.MessageBuild;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetectVersionClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(DetectVersionClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {

        if(msg.getDataType() != DataType.UpdateAsset) {
            ctx.fireChannelRead(msg);
            return;
        }
            if(!(msg.getUpdateAsset().getVersion() == 2.0f)){
                logger.info("需要update assets: {}", msg.getUpdateAsset());
                ConnectionMessage message = ConnectionMessage.
                        newBuilder().setDataType(DataType.RequestInfo)
                        .setRequestInfo(RequestInfo.newBuilder()
                                .setRequestType(RequestInfo.RequestType.UPDATE_ASSESTS)
                                .build()).build();
                ctx.writeAndFlush(message);
                ctx.pipeline().addAfter(this.getClass().getName(), FileReceiveServerHandler.class.getName(),
                        new FileReceiveClientHandler());
            }else {
                //not update
                String token = JsonUtils.localData.getString("token");
                if(!token.isEmpty()) {
                    ConnectionMessage message = MessageBuild.requestInfoBuild(token, RequestInfo.RequestType.VERIFY_TOKEN).build();
                    ctx.writeAndFlush(message);
                }
            }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("更新完毕.");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ConnectionMessage message = ConnectionMessage.
                newBuilder().setDataType(DataType.DetectVersion)
                .setDetectVersion(DetectVersion.newBuilder().
                        setClientVersion(2).build()).build();
        ctx.writeAndFlush(message);
    }
}
