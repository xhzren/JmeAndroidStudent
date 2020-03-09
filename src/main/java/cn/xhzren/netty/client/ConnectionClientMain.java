package cn.xhzren.netty.client;

import cn.xhzren.netty.util.JsonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionClientMain {

    static Logger logger = LoggerFactory.getLogger(ConnectionClientMain.class);

    private int port;
    private String fileName = "C:\\Users\\admin\\Downloads\\Compressed\\Res.zip";

    private EventLoopGroup group = new NioEventLoopGroup();

    private Bootstrap bs;

    public ConnectionClientMain(int port) throws Exception {
        this.port = port;
        JsonUtils.initDefaultLoad();
       }

    public void run() {
        bs = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .handler(new ConnectionClientInitialHandle());
        try {
//            ChannelFuture future = bs.connect("120.27.209.32", port).sync();
            ChannelFuture future = bs.connect("localhost", port).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        ConnectionClientMain clientMain = new ConnectionClientMain(9877);
        clientMain.run();
    }
}
