package cn.xhzren.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServerMain {

    private int port;

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();

    private ServerBootstrap bs;

    public WebSocketServerMain(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        WebSocketServerMain serverMain = new WebSocketServerMain(9877);
        serverMain.run();
    }

    public void run() {
        bs= new ServerBootstrap().group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        ch.pipeline().addLast(new HttpObjectAggregator(9128));
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws","dou"));
                        ch.pipeline().addLast(new ShakeHandsHandler());
                        ch.pipeline().addLast(new DramaMessageHandler());
                    }
                });
       try {
           ChannelFuture future = bs.bind(port).sync();
           future.channel().closeFuture();
       }catch (Exception e) {
           boss.shutdownGracefully();
           work.shutdownGracefully();
           e.printStackTrace();
       }
    }
}
