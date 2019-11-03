/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.test.water;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.SkyFactory;
import java.io.File;

/**
 *
 * @author xhzre
 */
public class SceneWater extends SimpleApplication implements ActionListener{
    
    public static void main(String[] args) {
        SceneWater app = new SceneWater();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50);
        Node mainScene = new Node();
        cam.setLocation(new Vector3f(-27.0f, 1.0f, 75.0f));
        cam.setRotation(new Quaternion(0.03f, 0.9f, 0f, 0.4f));
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Box tempBox = new Box(1, 1, 1);
        Geometry tempGeometry = new Geometry("Box", tempBox);
        tempGeometry.setMaterial(mat);
        mainScene.attachChild(tempGeometry);
        
        mainScene.attachChild(SkyFactory.createSky(assetManager,
            "Textures/Sky/Bright/BrightSky.dds", 
            SkyFactory.EnvMapType.CubeMap));
        
        File file = new File("wildhouse.zip");
        
        if(!file.exists()) {
            assetManager.registerLocator("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/wildhouse.zip", HttpZipLocator.class);
        }else {
            assetManager.registerLocator("wildhouse.zip", ZipLocator.class);
        }
        
        Spatial houseScene = assetManager.loadModel("main.scene");
        
        DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir=new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        houseScene.addLight(sun);
        
        rootNode.attachChild(houseScene);
        
        
        rootNode.attachChild(mainScene);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
    }
    
}
