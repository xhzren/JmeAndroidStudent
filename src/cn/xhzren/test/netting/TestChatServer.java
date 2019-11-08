/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.test.netting;

import com.jme3.network.*;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

import com.jme3.network.serializing.Serializer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class TestChatServer {

    public static final String NAME = "Test Chat Server";
    public static final int VERSION = 1;
    public static final int PORT = 9015;
    public static final int UDP_PORT = 9015;

    private Server server;
    private boolean isRunning;

    public TestChatServer() throws IOException {
        server = Network.createServer(NAME, VERSION, PORT, UDP_PORT);

        initializeClasses();
        ChatHandler handler = new ChatHandler();
        server.addMessageListener(handler);
        server.addConnectionListener(new ChatConnectionListener());
    }
    

    public boolean isRunning() {
        return isRunning;
    }

    public synchronized void start() {
        if(isRunning) {
            return;
        }else {
            server.start();
            isRunning = true;
        }
    }

    public synchronized void close() {
        if(!isRunning) {
            return;
        }

        for (HostedConnection conn : server.getConnections()) {
            conn.close("Server is shutting down.");
        }
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.close();
        isRunning = false;
        notifyAll();
    }

    public static void main(String[] args) throws Exception {
        
        System.out.println("Setting logging level to max");
        Logger networklog = Logger.getLogger("com.jme3.network");
        networklog.setLevel(Level.FINEST);

        Logger rootLog = Logger.getLogger("");
        if(rootLog.getHandlers().length > 0) {
            rootLog.getHandlers()[0].setLevel(Level.FINEST);
        }

        TestChatServer chatServer = new TestChatServer();
        chatServer.start();

        System.out.println("Waiting for connections on port: " + PORT);

        while (chatServer.isRunning) {
            synchronized (chatServer) {
                chatServer.wait();
            }
        }
    }
    public static void initializeClasses() {
        // Doing it here means that the client code only needs to
        // call our initialize.
        Serializer.registerClass(ChatMessage.class);
    }


    protected void runCommand(HostedConnection conn, String user, String command) {
        if("/shutdown".equals(command)) {
            server.broadcast(new ChatMessage("server", "Server is shutting down."));
            close();
        }else if("/help".equals(command)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Chat commands:\n");
            buffer.append("/help - prints this message.\n");
            buffer.append("/shutdown - shut down this server.\n");
            server.broadcast(new ChatMessage("server", buffer.toString()));
        }

    }

    private class ChatHandler implements MessageListener<HostedConnection> {
        public ChatHandler() {
        }
        @Override
        public void messageReceived(HostedConnection source, Message m) {
            if(m instanceof ChatMessage) {
                //track name
                ChatMessage cm = (ChatMessage)m;
                source.setAttribute("name", cm.getName());

                //check command
                if(cm.message.startsWith("/")) {
                    runCommand(source, cm.name, cm.message);
                }

                System.out.println("Broadcasting:" + m + "reliable:" + m.isReliable());
                source.getServer().broadcast(cm);
            }else {
                System.out.println("Received odd message: "+ m);
            }
        }
    }

    private class ChatConnectionListener implements ConnectionListener {
        @Override
        public void connectionAdded(Server server, HostedConnection conn) {
            System.out.println("connectionAdded(" + conn + ")");
        }
        @Override
        public void connectionRemoved(Server server, HostedConnection conn) {
            System.out.println("connectionRemoved(" + conn + ")");
        }
    }

    @Serializable
    public static class ChatMessage extends AbstractMessage {
        private String name;
        private String message;
        
        public ChatMessage() {
        }

        public ChatMessage(String name, String message) {
            setName(name);
            setMessage(message);
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "ChatMessage{" +
                    "name='" + name + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
