package cn.xhzren.netty.client;

import cn.xhzren.nettytest.proto.LoginProto.*;
import cn.xhzren.nettytest.proto.LoginProto.ConnectionMessage.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetectVersionClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(DetectVersionClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
        if(msg.getDataType() == DataType.UpdateAsset) {
            if(!(msg.getUpdateAsset().getVersion() == 2.0f)){
                logger.info("需要update assets: {}", msg.getUpdateAsset());
                ConnectionMessage message = ConnectionMessage.
                        newBuilder().setDataType(DataType.RequestInfo)
                        .setRequestInfo(RequestInfo.newBuilder()
                                .setRequestType(RequestInfo.RequestType.UPDATE_ASSESTS)
                                .build()).build();
                ctx.writeAndFlush(message);
                ctx.pipeline().addLast(new FileReceiveClientHandler());
            }
        }else {
            ctx.fireChannelRead(msg);
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
