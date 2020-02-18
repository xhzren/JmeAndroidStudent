/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.test.water;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;

/**
 *
 * @author xhzre
 */
public class SimpleWater extends SimpleApplication implements ActionListener{
    
    private Node sceneNode;
    private Material mat;
    private Geometry lightSphere;
    private Spatial waterPlane;
    private SimpleWaterProcessor waterProcessor;
    private Vector3f lightPos =  new Vector3f(33,12,-29);
    
    
    
    public static void main(String[] args) {
        SimpleWater app = new SimpleWater();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initScene();
        
        //create waterProcessor
        waterProcessor = new SimpleWaterProcessor(assetManager);
        
        //set reflect scene
        //only reflect scene is display water
        waterProcessor.setReflectionScene(sceneNode);
        waterProcessor.setDebug(true);
        //add to viewPort
        viewPort.addProcessor(waterProcessor);
        
        //create plane
        waterPlane = waterProcessor.createWaterGeometry(100, 100);
        waterPlane.setMaterial(waterProcessor.getMaterial());
//      waterPlane.setLocalScale(40);
        waterPlane.setLocalTranslation(-5, 0, 5);
        rootNode.attachChild(waterPlane);
        
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
    }
    
    private void initScene() {
        flyCam.setMoveSpeed(100);
        cam.setLocation(new Vector3f(0, 10, 10));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        
        sceneNode = new Node("Scene");
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Box tempBox = new Box(1, 1, 1);
        Geometry tempGeometry = new Geometry("Box", tempBox);
        tempGeometry.setMaterial(mat);
        sceneNode.attachChild(tempGeometry);
        
        sceneNode.attachChild(SkyFactory.createSky(assetManager, 
            "Textures/Sky/Bright/BrightSky.dds",
            SkyFactory.EnvMapType.CubeMap));
        rootNode.attachChild(sceneNode);
        
        Sphere lite = new Sphere(8, 8, 3.0f);
        lightSphere = new Geometry("lightsphere", lite);
        lightSphere.setMaterial(mat);
        lightSphere.setLocalTranslation(lightPos);
        rootNode.attachChild(lightSphere);
    }
    
}
