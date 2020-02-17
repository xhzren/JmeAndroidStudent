package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.data.PlayerData;
import cn.xhzren.game.monkeyzone.data.ServerClientData;
import cn.xhzren.game.monkeyzone.message.*;
import com.jme3.network.*;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerNetListener implements MessageListener<HostedConnection>, ConnectionListener {

    private ServerMain app;
    private Server server;
    private WorldManager worldManager;
    private ServerGameManager gameManager;
    private Logger LOGGER =  Logger.getLogger(this.getClass().getName());

    public ServerNetListener(ServerMain app,  Server server, WorldManager worldManager,ServerGameManager gameManager) {
        this.app = app;
        this.server = server;
        this.worldManager = worldManager;
        this.gameManager = gameManager;
        server.addConnectionListener(this);
        server.addMessageListener(this,
                HandshakeMessage.class,
                ClientJoinMessage.class,
                ChatMessage.class,
                StartGameMessage.class,
                ActionMessage.class);
    }


    @Override
    public void connectionAdded(Server server, HostedConnection client) {
        int clientId = client.getId();
        if(!ServerClientData.exsists(clientId)) {
            ServerClientData.add(clientId);
        }else {
            LOGGER.log(Level.SEVERE, "Client ID exists");
            return;
        }
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection client) {
        int clientId = client.getId();
        final long playerId = ServerClientData.getPlayerId(clientId);
        ServerClientData.remove(clientId);

        app.enqueue(() -> {
            String name = PlayerData.getStringData(playerId, "name");
            //TODO worldManager
            server.broadcast(new ChatMessage("Server", name + "left the world"));
            LOGGER.log(Level.INFO, "Broadcast player left message");
            if(PlayerData.getHumanPlayers().isEmpty()) {
                //TODO gameManager
            }
            return null;
        });
    }

    @Override
    public void messageReceived(HostedConnection source, Message message) {
        //第一次握手
        if(message instanceof HandshakeMessage) {
            HandshakeMessage msg = (HandshakeMessage)message;
            LOGGER.log(Level.INFO, "Got handshake message");
            if(msg.protocol_version != Globals.PROTOCOL_VERSION) {
                source.close("Connection Protocol Mismatch - Update Client");
                LOGGER.log(Level.INFO, "Client protocol mismatch - disconnecting");
                return;
            }
            msg.server_version = Globals.SERVER_VERSION;
            source.send(msg);
            LOGGER.log(Level.INFO, "Sent back handshake message");
        }
        else if(message instanceof ClientJoinMessage) {
            final ClientJoinMessage msg = (ClientJoinMessage)message;
            LOGGER.log(Level.INFO, "Got client join message");
            final int clientId = source.getId();
            //TODO login check
            if(!ServerClientData.exsists(clientId)) {
                LOGGER.log(Level.WARNING, "Receiving join message from unknown client");
                return;
            }
            //TODO create new player,reuse old on drop possible?
            final long newPlayerId = PlayerData.getNew(msg.name);
            LOGGER.log(Level.INFO, "Create new player ID {0}", newPlayerId);
            ServerClientData.setConnected(clientId, true);
            ServerClientData.setPlayerId(clientId, newPlayerId);

            ServerJoinMessage serverJoinMessage = new ServerJoinMessage(false, newPlayerId, clientId, msg.name);
            server.broadcast(serverJoinMessage);

            LOGGER.log(Level.INFO, "Login succesful - sent back join message");

            //add the player

            app.enqueue(new Callable<Void>() {
                //TODO client id as group id
                @Override
                public Void call() {

                    return null;
                }
            });
        }else if(message instanceof ChatMessage) {
            ChatMessage msg = (ChatMessage)message;
            System.out.println(msg);
        }else if(message instanceof StartGameMessage) {
            StartGameMessage msg = (StartGameMessage)message;
            System.out.println(msg);
        }else if(message instanceof ActionMessage) {
            ActionMessage msg = (ActionMessage)message;
            System.out.println(msg);
        }
    }
}
