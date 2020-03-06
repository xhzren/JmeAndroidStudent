package cn.xhzren.netty.appstates;

import cn.xhzren.netty.util.Constancts;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransitionSceneAppState extends BaseAppState {

    static Logger logger = LoggerFactory.getLogger(TransitionSceneAppState.class);

    private SimpleApplication application;
    private Node root;
    private Texture bg;
    private Texture tip;
    @Override
    protected void initialize(Application app) {
        application = (SimpleApplication)app;
        root = new Node("transitionScene");
//        bg = application.getAssetManager().loadTexture(Constancts.getTransitionBg());
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        application.getRootNode().attachChild(root);
    }

    @Override
    protected void onDisable() {
        root.removeFromParent();
    }
}
