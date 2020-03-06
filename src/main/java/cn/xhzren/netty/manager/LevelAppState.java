package cn.xhzren.netty.manager;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.fbx.SceneKey;

public class LevelAppState extends BaseAppState {

    private Node level;
    private SceneKey defaultKey;
    private Spatial currentScene;
    private SimpleApplication application;

    public LevelAppState(String name) {
        assert name == null && name.isEmpty();
        defaultKey = new SceneKey(name);
    }

    @Override
    protected void initialize(Application app) {
        application = (SimpleApplication)app;
        level = (Node)application.getRootNode().getChild("level");
        currentScene = app.getAssetManager().loadAsset(defaultKey);
        level.attachChild(currentScene);
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
