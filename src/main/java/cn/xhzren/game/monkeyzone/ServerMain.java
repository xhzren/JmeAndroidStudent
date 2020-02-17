package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.message.ActionMessage;
import cn.xhzren.game.monkeyzone.message.AutoControlMessage;
import cn.xhzren.game.monkeyzone.message.ManualControlMessage;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.network.Network;
import com.jme3.network.Server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain extends SimpleApplication {

    private static Server server;
    private static ServerMain app;

    public static void main(String[] args) {
        Util.registerSerializers();
        Util.setLogLevels(true);
        app = new ServerMain();
        app.start();
    }

    private WorldManager worldManager;
    private PhysicsSyncManager syncManager;
    private ServerNetListener listenerManager;
    private ServerGameManager gameManager;
    private BulletAppState bulletAppState;


    @Override
    public void simpleInitApp() {
        try {
            server = Network.createServer(Globals.DEFAULT_PORT_TCP,
                    Globals.DEFAULT_PORT_UDP);
            server.start();
        }catch (IOException e) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, "Don't start server: {0}", e);
            return;
        }

        bulletAppState = new BulletAppState();
        getStateManager().attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setAccuracy(Globals.PHYSICS_FPS);
        //create sync manager
        syncManager = new PhysicsSyncManager(server, app);
        syncManager.setSyncFrequency(Globals.NETWORK_SYNC_FREQUENCY);
        syncManager.setMessageTypes(AutoControlMessage.class,
                ActionMessage.class,
                ManualControlMessage.class);
        stateManager.attach(syncManager);
        //cerate world manager
        worldManager = new WorldManager(app, rootNode);
        stateManager.attach(worldManager);
        //register world manager with sync manager so that messages can apply their data
        //向同步管理器注册世界管理器,以便消息可以应用其数据
        syncManager.addObject(-1, worldManager);
        //create server side game manager
        gameManager = new ServerGameManager();
        stateManager.attach(gameManager);
        listenerManager = new ServerNetListener(this, server, worldManager, gameManager);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void destroy() {
        super.destroy();
        server.close();
    }
}
