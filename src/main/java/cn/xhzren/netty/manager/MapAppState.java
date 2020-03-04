package cn.xhzren.netty.manager;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.fbx.SceneKey;

import java.util.List;

public class MapAppState extends BaseAppState {

    private Node mapRoot;
    private SceneKey current;
    private List<SceneKey> sceneKeys;

    public MapAppState() {
        mapRoot = new Node("mapRoot");
        current = new SceneKey("");
    }

    @Override
    protected void initialize(Application app) {
        app.getAssetManager().loadAsset(sceneKeys.get(0));
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
