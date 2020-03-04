package cn.xhzren.netty.client;

import cn.xhzren.netty.util.FileHelper;
import cn.xhzren.nettytest.proto.LoginProto.*;
import cn.xhzren.nettytest.proto.LoginProto.ReceiveInfo.*;
import cn.xhzren.nettytest.proto.LoginProto.ConnectionMessage.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileReceiveClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {
    static Logger logger = LoggerFactory.getLogger(FileReceiveClientHandler.class);

    byte[] res;
    FileChannel channel;
    long spawnTime;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) throws Exception {
        if(msg.getDataType() == ConnectionMessage.DataType.FileReceive) {

            FileReceive receive = msg.getFileReceive();
            res = receive.getData().toByteArray();
            logger.info("接收文件数据: 进度为->{}, size->{}, start->{},end->{}",
                    (receive.getEnd() / receive.getSize())* 0.1,receive.getSize(),
                    receive.getStart(), receive.getEnd());
            channel.write(ByteBuffer.wrap(res));
            if(receive.getEnd() == receive.getSize()) {
                logger.info("共用时: {}", System.currentTimeMillis() - spawnTime);
//                ctx.writeAndFlush(buildMessage(ReceiveStatus.SUCCESS, ReceiveType.FILE_RECEIVE));
                ctx.pipeline().remove(this);
                ctx.pipeline().addLast(new LoginClientHandler());
            }
            ctx.writeAndFlush(buildMessage(ReceiveStatus.SUCCESS, ReceiveType.FILE_RECEIVE));
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("文件接收 客户端启动");
        channel = new RandomAccessFile(FileHelper.makeTmpFile("zip"), "rw").getChannel();
        spawnTime = System.currentTimeMillis();
//        ctx.writeAndFlush(buildMessage(ReceiveStatus.SUCCESS, ReceiveType.FILE_RECEIVE));
    }

    private ConnectionMessage buildMessage(ReceiveStatus status, ReceiveType type) {
        return  ConnectionMessage.newBuilder()
                .setDataType(DataType.ReceiveInfo)
                .setReceiveInfo(ReceiveInfo.newBuilder().
                        setReceiveStatus(status).setReceiveType(type)
                        .build()).build();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("文件接收 客户端关闭");
        channel.close();
    }
}
