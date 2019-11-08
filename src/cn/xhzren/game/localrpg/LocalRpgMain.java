/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.game.localrpg;

import cn.xhzren.game.localrpg.build.SpatialBuild;
import cn.xhzren.game.localrpg.entity.Life;
import cn.xhzren.game.localrpg.template.LifeTemplate;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState;   
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import jme3tools.optimize.TextureAtlas;

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
        Spatial player = SpatialBuild.getTextureSpatial("player");
        player.setUserData("life", LifeTemplate.HUMANOID("test"));
        Life playLife = player.getUserData("life");
        System.out.println(playLife);
        player.move(settings.getWidth()/2, settings.getHeight()/2, 0);
        dynamicNode.attachChild(player);
    }

    private void initGui() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();

        Container rootWindow = new Container();
        staticNode.attachChild(rootWindow);
        Button start = new Button("Start");
        start.setSize(new Vector3f(10, 10, 20));
        rootWindow.attachChild(start);
    }

    @Override
    public void onAction(String name, boolean pressed, float ftp) {

    }
}
