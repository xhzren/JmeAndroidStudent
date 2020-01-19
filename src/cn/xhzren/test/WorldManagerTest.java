package cn.xhzren.test;

import cn.xhzren.game.monkeyzone.Globals;
import cn.xhzren.game.monkeyzone.Util;
import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.message.StartGameMessage;
import com.jme3.network.Client;
import com.jme3.network.Network;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldManagerTest {

    private static Client client;

    public static void main(String[] args) {
        Util.registerSerializers();
        Util.setLogLevels(true);
        try {
            client = Network.connectToServer(Globals.DEFAULT_SERVER, Globals.DEFAULT_PORT_TCP, Globals.DEFAULT_PORT_UDP);
            client.start();
        }catch (IOException e) {
            Logger.getLogger(WorldManagerTest.class.getName()).log(Level.SEVERE, "Do'nt Start Client! {0}", e);
            return;
        }

        client.send(new StartGameMessage("testMap", new String[]{"Sin","Xiao"}));
    }
}
