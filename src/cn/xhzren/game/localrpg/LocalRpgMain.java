/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.game.localrpg;

import cn.xhzren.game.localrpg.appstate.PlayerAppState;
import cn.xhzren.game.localrpg.build.SpatialBuild;
import cn.xhzren.game.localrpg.entity.Life;
import cn.xhzren.game.localrpg.template.LifeTemplate;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 *
 * @author admin
 */
public class LocalRpgMain extends SimpleApplication implements ActionListener {
    
    public static Node dynamicNode;
    public static Node staticNode;
    public static AssetManager assetManagerClose;

    public static void main(String[] args) {
        LocalRpgMain app = new LocalRpgMain();
        app.setShowSettings(false);
        app.start();
    }


    @Override
    public void simpleInitApp() {
        stateManager.attach(new PlayerAppState());
        stateManager.detach(new PlayerAppState());
        stateManager.cleanup();
        new PlayerAppState();
        assetManagerClose = assetManager;
        init2DSettings();
        initLife();
        initGui();
    }
    
    private void init2DSettings() {
        flyCam.setEnabled(false);
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0, 0, 0.5f));
        setDisplayStatView(false);
//      setDisplayFps(false);

        staticNode = new Node("staticNode");
        staticNode.setQueueBucket(RenderQueue.Bucket.Gui);

        dynamicNode = new Node("dynamicNode");
        dynamicNode.setQueueBucket(RenderQueue.Bucket.Gui);
        dynamicNode.move(0, 0, 10);

        guiNode.attachChild(staticNode);
        guiNode.attachChild(dynamicNode);

    }

    private void initLife() {
        Node player = (Node)SpatialBuild.getTextureSpatial("player");
        player.setUserData("life", LifeTemplate.HUMANOID("test"));
        Life playLife = player.getUserData("life");
        System.out.println(playLife);
        player.move(settings.getWidth()/2, settings.getHeight()/2, 0);
        Picture picture = (Picture)player.getChild("player_pic");
        dynamicNode.attachChild(player);
    }

    private void initGui() {
//        GuiGlobals.initialize(this);
//        BaseStyles.loadGlassStyle();
//
//        Container rootWindow = new Container();
//        staticNode.attachChild(rootWindow);
//        Button start = new Button("Start");
//        start.setSize(new Vector3f(10, 10, 20));
//        rootWindow.attachChild(start);
    }

    @Override
    public void onAction(String name, boolean pressed, float ftp) {

    }
}
