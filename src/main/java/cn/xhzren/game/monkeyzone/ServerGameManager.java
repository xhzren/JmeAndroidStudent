package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.message.StartGameMessage;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 * Manages the actual gameplay on the server side
 * 在服务端管理实际的游戏玩法
 * @author normenhansen
 */
public class ServerGameManager extends AbstractAppState {

    PhysicsSyncManager server;
    WorldManager worldManager;
    private boolean running;
    String mapName;
    String[] modelNames;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.worldManager = stateManager.getState(WorldManager.class);
        this.server = worldManager.getSyncManager();
    }

    public synchronized boolean startGame(String map) {
        if(running) {
            return false;
        }
        running = true;
        mapName = map;
        //TODO: parse client side string, create preload model list automatically
        //解析客户端字符串, 自动创建预载模型列表
        modelNames = new String[]{"Models/HoverTank/HoverTank.j3o", "Models/Sinbad/Sinbad.j3o", "Models/Ferrari/Car.j3o"};//, "Models/Buggy/Buggy.j3o"}
        server.getServer().broadcast(new StartGameMessage(mapName, modelNames));
        worldManager.loadLevel(mapName);
//        worldManager.createNavMesh();
//        worldManager.preloadModels(modelNames);
        worldManager.attachLevel();
        System.out.println("启动游戏");
        return true;
    }
}
