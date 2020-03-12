package cn.xhzren.netty.client;

import cn.xhzren.netty.SimpleMain;
import cn.xhzren.netty.appstates.PlayerListAppState;
import cn.xhzren.netty.appstates.TransitionSceneAppState;
import cn.xhzren.netty.entity.LocalAccountData;
import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.servers.RedisHelper;
import cn.xhzren.netty.util.JsonUtils;
import cn.xhzren.netty.util.MessageBuild;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginClientHandler extends SimpleChannelInboundHandler<ConnectionMessage> {

    static Logger logger = LoggerFactory.getLogger(LoginClientHandler.class);

    public static String token;

    private SimpleMain app;

    public LoginClientHandler(SimpleMain app) {
        this.app = app;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionMessage msg) {
        if (msg.getReceiveInfo().getReceiveType() == ReceiveType.LOGIN_RECEIVE) {
            if (msg.getReceiveInfo().getReceiveStatus() == ReceiveStatus.SUCCESS) {
                logger.info("login success: {}", msg.getReceiveInfo().getContent());
                token = msg.getReceiveInfo().getContent();
                LocalAccountData newToken = new LocalAccountData();
                newToken.setToken(token);
                newToken.setUsername(msg.getLogin().getName());
                newToken.setActive(true);
                JsonUtils.localData.add(newToken);
                JsonUtils.writeLocalData();

                //hide login
//                app.getStateManager().getState(TransitionSceneAppState.class)
//                        .isClose = true;
                app.getStateManager().attach(new PlayerListAppState());
            } else {
                app.getStateManager().getState(TransitionSceneAppState.class)
                        .popupTips(msg.getReceiveInfo().getContent());
            }
        } else if (msg.getDataType() == DataType.PlayerList) {
            app.getStateManager().getState(PlayerListAppState.class)
                    .makePlayerList(msg.getPlayerList());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}

