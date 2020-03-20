package cn.xhzren.netty.appstates;

import cn.xhzren.SimpleMainServer;
import cn.xhzren.netty.SimpleMain;
import cn.xhzren.netty.client.ConnectionClientMain;
import cn.xhzren.netty.client.DetectVersionClientHandler;
import cn.xhzren.netty.client.LoginClientHandler;
import cn.xhzren.netty.client.StatusClientHandler;
import cn.xhzren.netty.servers.ConnectionServerMain;
import cn.xhzren.netty.util.Constancts;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;

public class ServerAppState extends BaseAppState {

    private SimpleMainServer app;
    public ConnectionServerMain server;
    public Channel channel;
    public ChannelPipeline pipeline;

    @Override
    protected void initialize(Application app) {
        this.app = (SimpleMainServer) app;
        server = new ConnectionServerMain(Constancts.PORT, (SimpleMainServer)app);
        server.run();
    }

    public void setParam(Channel channel) {
        this.channel = channel;
        this.pipeline = channel.pipeline();
    }


    @Override
    protected void cleanup(Application app) {
        server.display();
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
