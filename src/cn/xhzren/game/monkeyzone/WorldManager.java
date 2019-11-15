package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.physicssync.PhysicsSyncMessage;
import cn.xhzren.game.monkeyzone.physicssync.SyncMessageValidator;
import cn.xhzren.game.monkeyzone.util.NavMesh;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.network.Client;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.HashMap;

public class WorldManager extends AbstractAppState implements SyncMessageValidator {

    private Server server;
    private Client client;
    private long myPlayerId = -2;
    private long myGroupId = -2;
    private NavMesh navMesh = new NavMesh();
    private Node rootNode;
    private Node worldNode;
    private HashMap<Long, Spatial> entities = new HashMap<>();
    private int newId = 0;
    private Application app;
    private AssetManager assetManager;

    @Override
    public boolean checkMessage(PhysicsSyncMessage message) {
        return false;
    }
}
