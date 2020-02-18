package cn.xhzren.game.mutil;

import cn.xhzren.game.mutil.message.HelloMessage;
import cn.xhzren.game.mutil.messagelistener.ClientListener;
import cn.xhzren.game.mutil.util.RegisterUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Network;
import com.jme3.system.JmeContext;

import java.io.IOException;

public class SampleClient extends SimpleApplication implements ClientStateListener {
    private Client client;

    public static void main(String[] args) {
        RegisterUtils.initSerializable();
        SampleClient app = new SampleClient();
        app.start(JmeContext.Type.Display);
    }

    @Override
    public void simpleInitApp() {
        try {
            client = Network.connectToServer("localhost", 9031);
            initMessageListener();
        }catch (IOException e) {
            e.printStackTrace();
        }
        client.start();

        client.send(new HelloMessage("测试连接"));
    }

    private void initMessageListener() {
        client.addMessageListener(new ClientListener(), HelloMessage.class);
        client.addClientStateListener(this);
    }

    @Override
    public void update() {
    }

    @Override
    public void clientConnected(Client source) {
        System.out.println("连接到服务器");
    }

    @Override
    public void clientDisconnected(Client source, DisconnectInfo info) {
        System.out.println("断开了连接");
        System.out.println("info: " + info);
    }

    @Override
    public void destroy() {
        client.close();
        super.destroy();
    }
}
