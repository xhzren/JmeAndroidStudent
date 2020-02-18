package cn.xhzren.game.mutil.messagelistener;

import cn.xhzren.game.mutil.message.HelloMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ClientListener implements MessageListener<Client> {
    @Override
    public void messageReceived(Client source, Message msg) {
        if(msg instanceof HelloMessage) {
            HelloMessage helloMessage = (HelloMessage)msg;
            System.out.println("Client #"+source.getId()+" received: '"+helloMessage+"'");
        }
    }
}
