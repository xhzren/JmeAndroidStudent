package cn.xhzren.game.mutil;

import cn.xhzren.game.mutil.message.HelloMessage;
import cn.xhzren.game.mutil.util.RegisterUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.network.*;
import com.jme3.system.JmeContext;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerAppLication extends SimpleApplication implements ConnectionListener {
    private static final Logger LOGGER = Logger.getLogger(ServerAppLication.class.getName());
    private Server server;

    public static void main(String[] args) {
        RegisterUtils.initSerializable();
        ServerAppLication app = new ServerAppLication();
        app.start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp() {
        try {
            server = Network.createServer(9031);
            initMessageListener();
        }catch (IOException e) {
            e.printStackTrace();
        }

        server.start();
    }

    private void initMessageListener() {
        server.addMessageListener(new HostedConnectionListener(), HelloMessage.class);
        server.addConnectionListener(this);
    }

    public class HostedConnectionListener implements MessageListener<HostedConnection> {
        @Override
        public void messageReceived(HostedConnection source, Message msg) {
            if(msg instanceof HelloMessage) {
                HelloMessage helloMessage = (HelloMessage)msg;
                LOGGER.log(Level.INFO, "Server #{0} received: {1}",
                        new Object[]{source.getId(), helloMessage});

                server.broadcast(new HelloMessage("hao are you."));
            }
        }
    }

    @Override
    public void update() {
        server.getConnections();
    }

    @Override
    public void connectionAdded(Server server, HostedConnection conn) {
        System.out.println("id: " + conn.getId() + "加入了连接.");
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection conn) {
        System.out.println("id: " + conn.getId() + "断开了连接.");
    }

    @Override
    public void destroy() {
        server.close();
        super.destroy();
    }
}
