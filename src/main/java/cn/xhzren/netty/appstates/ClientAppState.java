package cn.xhzren.netty.appstates;

import cn.xhzren.netty.SimpleMain;
import cn.xhzren.netty.client.ConnectionClientMain;
import cn.xhzren.netty.client.DetectVersionClientHandler;
import cn.xhzren.netty.client.LoginClientHandler;
import cn.xhzren.netty.client.StatusClientHandler;
import cn.xhzren.netty.util.Constancts;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;

public class ClientAppState extends BaseAppState {

    private SimpleMain app;
    public ConnectionClientMain client;
    public Channel channel;
    public ChannelPipeline pipeline;

    @Override
    protected void initialize(Application app) {
        this.app = (SimpleMain)app;
        client = new ConnectionClientMain(Constancts.PORT, (SimpleMain)app);
        client.run();
    }

    public void setParam(Channel channel) {
        this.channel = channel;
        this.pipeline = channel.pipeline();
        pipeline.addLast(new StatusClientHandler(app));
        pipeline.addLast(new DetectVersionClientHandler(app));
        pipeline.addLast(new LoginClientHandler(app));
        getState(StartAppState.class).sendVersion(channel);
    }

    @Override
    protected void cleanup(Application app) {
        client.getGroup().shutdownGracefully();
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
