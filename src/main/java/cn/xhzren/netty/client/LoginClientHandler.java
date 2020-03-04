package cn.xhzren.netty.client;

import cn.xhzren.nettytest.proto.LoginProto.*;
import cn.xhzren.nettytest.proto.LoginProto.ReceiveInfo.*;
import cn.xhzren.nettytest.proto.LoginProto.ConnectionMessage.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(LoginClientHandler.class);

    private PlayerList playerList;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
//        if(msg.getDataType() == DataType.PlayerList) {
//            PlayerList playerList = msg.getPlayerList();
//            playerList.getPlayerInfoList().forEach((e)-> {
//                logger.info("player : {}", e);
//            });
//        }
        if(msg.getDataType() == DataType.ReceiveInfo) {
            if(msg.getReceiveInfo().getReceiveStatus() == ReceiveStatus.SUCCESS) {
                logger.info("login success: {}", msg.getReceiveInfo().getContent());
            }else if(msg.getReceiveInfo().getReceiveStatus() == ReceiveStatus.ERROR) {
                logger.info("login error: {}", msg.getReceiveInfo().getContent());
            }
        }else if(msg.getDataType() == DataType.PlayerList) {
            PlayerList playerList = msg.getPlayerList();
            playerList.getPlayerInfoList().forEach((e)-> {
                logger.info("player : {}", e);
            });
            this.playerList = playerList;
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("login add");
        ConnectionMessage login = ConnectionMessage.newBuilder()
                .setDataType(ConnectionMessage.DataType.LoginType)
                .setLogin(Login.newBuilder().setId(1).setName("test")
                        .setLoginType(Login.LoginType.SELF)
                        .setPassWord("123455").build()).build();
        ctx.writeAndFlush(login);
    }

    public PlayerList getPlayerList() {
        return playerList;
    }

    public void setPlayerList(PlayerList playerList) {
        this.playerList = playerList;
    }
}
