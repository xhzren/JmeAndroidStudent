package cn.xhzren.netty.client;

import cn.xhzren.netty.SimpleMain;
import cn.xhzren.netty.appstates.ClientAppState;
import cn.xhzren.netty.entity.LoginProto;
import cn.xhzren.netty.util.Constancts;
import cn.xhzren.netty.util.JsonUtils;
import com.jme3.system.JmeContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionClientMain {

    static Logger logger = LoggerFactory.getLogger(ConnectionClientMain.class);

    private int port;
    private String fileName = "C:\\Users\\admin\\Downloads\\Compressed\\Res.zip";

    public EventLoopGroup group = new NioEventLoopGroup();

    public Bootstrap bs;

    public Channel channel;

    public SimpleMain app;


    public ConnectionClientMain(int port, SimpleMain app) {
        this.port = port;
        this.app = app;
       }

    public void run() {
        bs = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .handler(new ConnectionClientInitialHandle());
        try {
//            ChannelFuture future = bs.connect("120.27.209.32", port).sync();
           ChannelFuture future = bs.connect("localhost", Constancts.PORT)
//           channel = future.channel();
                    .addListener((ChannelFuture e)-> {
                        if(e.isSuccess()) {
                            channel = e.channel();
                            app.getStateManager().getState(ClientAppState.class)
                                    .setParam(channel);
                            logger.info("channel success");
                        }
                        });
        }catch (Exception e) {
            group.shutdownGracefully();
            e.printStackTrace();
        }finally {
//            group.shutdownGracefully();
        }
    }

    public EventLoopGroup getGroup() {
        return group;
    }

    public Bootstrap getBs() {
        return bs;
    }

    public Channel getChannel() {
        return channel;
    }

    public static void main(String[] args) throws Exception {
//        ConnectionClientMain clientMain = new ConnectionClientMain(Constancts.PORT);
//        clientMain.run();

    }
}
