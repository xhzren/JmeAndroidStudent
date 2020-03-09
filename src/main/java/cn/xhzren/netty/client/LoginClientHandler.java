package cn.xhzren.netty.client;

import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.servers.RedisHelper;
import cn.xhzren.netty.util.JsonUtils;
import cn.xhzren.netty.util.MessageBuild;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(LoginClientHandler.class);

    private PlayerList playerList;

    public static String token;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
        System.out.println(msg.getReceiveInfo().getReceiveType());
        if(msg.getReceiveInfo().getReceiveType() == ReceiveType.LOGIN_RECEIVE) {
            if(msg.getReceiveInfo().getReceiveStatus() == ReceiveStatus.SUCCESS) {
                logger.info("login success: {}", msg.getReceiveInfo().getContent());
                token = msg.getReceiveInfo().getContent();
                JsonUtils.localData.put("token", token);
                JsonUtils.writeLocalData();

                ConnectionMessage message = MessageBuild.requestInfoBuild(token, RequestInfo.RequestType.PLAYER_LIST).build();
                ctx.writeAndFlush(message);
            }
        }else if(msg.getDataType() == DataType.PlayerList) {
            PlayerList playerList = msg.getPlayerList();
            playerList.getPlayerInfoList().forEach((e)-> {
                logger.info("player : {}", e);
            });
            this.playerList = playerList;
            ctx.pipeline().addLast(new TaskClientHandler());
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    public PlayerList getPlayerList() {
        return playerList;
    }

    public void setPlayerList(PlayerList playerList) {
        this.playerList = playerList;
    }
}
