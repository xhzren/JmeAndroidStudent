package cn.xhzren.netty.client;

import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.util.JsonUtils;
import cn.xhzren.netty.util.MessageBuild;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(TaskClientHandler.class);


    private ChannelHandlerContext ctx;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
        this.ctx = ctx;

        if(msg.getDataType() != DataType.TaskInfo) {
            ctx.fireChannelRead(msg);
        }

    }

    public void addTask(TaskInfo taskInfo) {
        ConnectionMessage message = MessageBuild.requestInfoBuild(RequestInfo.RequestType.ADD_TASK)
                .setDataType(DataType.TaskInfo).setTaskInfo(taskInfo).build();
        ctx.writeAndFlush(message);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        TaskInfo taskInfo = TaskInfo.newBuilder()
                .setId("test").setName("Test").setTaskStatus(TaskInfo.TaskStatus.TASK_PROGRESS).build();
        addTask(taskInfo);
    }
}
