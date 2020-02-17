package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.message.*;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the network message transfer for the client in a threadsafe way
 * 以线程安全的方式处理客户端的网络消息传输
 * @author normenhansen
 */
public class ClientNetListener implements MessageListener, ClientStateListener {

    private ClientMain app;
    private Client client;
    private String name = "";
    private String pass = "";
    private WorldManager worldManager;

    public ClientNetListener(ClientMain app, Client client, WorldManager worldManager) {
        this.app = app;
        this.client = client;
        this.worldManager = worldManager;
        client.addClientStateListener(this);
        client.addMessageListener(this, HandshakeMessage.class,
                ServerJoinMessage.class, StartGameMessage.class,
                ChatMessage.class, ServerAddPlayerMessage.class,
                ServerRemovePlayerMessage.class);
    }

    @Override
    public void clientConnected(Client client) {
        setStatusText("Requesting login..");
        HandshakeMessage msg = new HandshakeMessage(Globals.PROTOCOL_VERSION, Globals.CLIENT_VERSION,
                Globals.SERVER_VERSION);
        client.send(msg);
        Logger.getLogger(ClientNetListener.class.getName()).log(Level.INFO, "Sent handshake message");
    }

    @Override
    public void clientDisconnected(Client client, DisconnectInfo disconnectInfo) {
        setStatusText("Server connection failed!");
    }

    @Override
    public void messageReceived(Object o, Message message) {
        if(message instanceof HandshakeMessage) {
            HandshakeMessage msg = (HandshakeMessage)message;
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "Got handshake message back");
            if(msg.protocol_version != Globals.PROTOCOL_VERSION) {
                setStatusText("Protocol mismatch - update client!");
                Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                        "Client protocol mismatch, disconnecting");
                return;
            }
            client.send(new ClientJoinMessage(name, pass));
        }else if(message instanceof  ServerJoinMessage) {
            final ServerJoinMessage msg = (ServerJoinMessage)message;
            if(!msg.rejected) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                        "Connected");
                app.enqueue(()->{
                    worldManager.setMyPlayerId(msg.id);
                    worldManager.setMyGroupId(msg.group_id);
                    app.lobby();
                    return null;
                });
            }else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                  "Server ditched us! Cant login");
                setStatusText("Server rejected login");
            }
        }else if(message instanceof StartGameMessage) {
            final StartGameMessage msg = (StartGameMessage)message;
            //loadlevel is threaded and cares for OpenGL thread itself
            app.loadLevel(msg.levelName, msg.modelNames);
        }else if(message instanceof ChatMessage) {
            final ChatMessage msg = (ChatMessage)message;
            app.addChat(msg.name + ": " + msg.text);
        }else if(message instanceof ServerAddPlayerMessage) {
            app.updatePlayerData();
        }else if(message instanceof ServerRemovePlayerMessage) {
            app.updatePlayerData();
        }
    }

    private void setStatusText(String text)  {
        app.setStatusText(text);
    }

    public String getName() {
        return name;
    }

    /**
     * sets the login name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    /**
     * sets the login password
     *
     * @param pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
}
