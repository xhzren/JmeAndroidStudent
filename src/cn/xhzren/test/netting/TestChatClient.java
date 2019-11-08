/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.test.netting;

import com.jme3.network.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import cn.xhzren.test.netting.TestChatServer.ChatMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class TestChatClient extends JFrame {
    
    private Client client;
    private final JEditorPane chatLog;
    private StringBuffer chatMessages = new StringBuffer();
    private JTextField nameField;
    private JTextField messageField;
    
    public TestChatClient(String host) throws Exception{
        super("Test Chat Client - to" + host);
        System.out.println(host);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        chatLog = new JEditorPane();
        chatLog.setEditable(false);
        chatLog.setContentType("text/html");
        chatLog.setText("<html><body</body></html>");
        
        getContentPane().add(new JScrollPane(chatLog), "Center");

        //netting
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,  BoxLayout.X_AXIS));
        panel.add(new JLabel("Name:"));
        nameField = new JTextField(System.getProperty("user.name", "yourname"));
        Dimension dimension = nameField.getPreferredSize();
        nameField.setMaximumSize(new Dimension(120, dimension.height + 6));
        panel.add(nameField);
        panel.add(new JLabel(" Message:"));
        messageField = new JTextField();
        panel.add(messageField);
        panel.add(new JButton(new SendAction(true)));
        panel.add(new JButton(new SendAction(false)));

        getContentPane().add(panel, "South");

        client = Network.connectToServer(TestChatServer.NAME,
                TestChatServer.VERSION, host, TestChatServer.PORT, TestChatServer.UDP_PORT);
        client.addErrorListener(new ChatErrorListener());
        client.addMessageListener(new ChatChandler(), ChatMessage.class);
        client.addClientStateListener(new ChatClientStateListener());
        client.start();

        System.out.println("client started: " + client);
    }

    @Override
    public void dispose() {
        System.out.println("Chat windows close!");
        super.dispose();
        if(client.isConnected()) {
            client.close();
        }
    }
   
    public static void main(String[] args) throws Exception {
         // Increate the logging level for networking...
        System.out.println("Setting logging to max");
        Logger networkLog = Logger.getLogger("com.jme3.network"); 
        networkLog.setLevel(Level.FINEST);
 
        // And we have to tell JUL's handler also   
        // turn up logging in a very convoluted way
        Logger rootLog = Logger.getLogger("");
        if( rootLog.getHandlers().length > 0 ) {
            rootLog.getHandlers()[0].setLevel(Level.FINEST);
        }
        
        String host = getHost(null, "Input Host", "Plase Input your Host!", "9012");
        if (host == null) {
            System.out.println("User cancelled.");
            return;
        }
        
        // Register a shutdown hook to get a message on the console when the
        // app actually finishes
        Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Chat client is terminating.");
                }
            });
        
        TestChatClient app = new TestChatClient(host);
        app.setVisible(true);
    }

    public static String getHost(Component owner, String title, String message, String initialValue) {
        return (String)JOptionPane.showInputDialog(owner, message, title, JOptionPane.PLAIN_MESSAGE,
                null, null, initialValue);
    }

    private class ChatChandler implements MessageListener<Client> {
        @Override
        public void messageReceived(Client source, Message m) {
            ChatMessage chat = (ChatMessage)m;

            System.out.println("Received:" + m);

            chatMessages.append("<font color='#00a000'>" + (m.isReliable() ? "TCP":"UDP") + "</font>");
            chatMessages.append("<font color='#000080'><b>" + chat.getName() + "</b></font>");
            chatMessages.append(chat.getMessage());
            chatMessages.append("<br />");
            String s = "<html><body>" + chatMessages + "</body></html>";
            chatLog.setText(s);
            chatLog.select(s.length(), s.length());

        }
    }

    private class ChatClientStateListener implements ClientStateListener {
        @Override
        public void clientConnected(Client client) {
            System.out.println("clientConnected(" + client + ")");
        }
        @Override
        public void clientDisconnected(Client client, DisconnectInfo disconnectInfo) {
            System.out.println("clientDisconnected(" + client + "):" + disconnectInfo);
            if(disconnectInfo != null) {
                JOptionPane.showMessageDialog(rootPane,
                        disconnectInfo.reason,
                        "Disconnection closed",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class ChatErrorListener implements ErrorListener<Client> {
        @Override
        public void handleError(Client source, Throwable t) {
            System.out.println("handleError(" + source + ",  " + t + ")");
            JOptionPane.showMessageDialog(rootPane,
                                            String.valueOf(t),
                                            "Connection Error",
                                            JOptionPane.ERROR_MESSAGE);
        }
    }



    private class SendAction extends AbstractAction {
        private final boolean reliable;
        public SendAction(boolean reliable) {
            super(reliable ? "TCP" : "UDP");
            this.reliable = reliable;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String message = messageField.getText();

            ChatMessage chat = new ChatMessage(name, message);
            chat.setReliable(reliable);
            System.out.println("Sending:" + chat);
            client.send(chat);
        }
    }
}
