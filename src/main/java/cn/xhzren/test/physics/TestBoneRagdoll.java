package cn.xhzren.test.physics;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import groovy.ui.Console;

import java.security.PublicKey;

public class TestBoneRagdoll extends SimpleApplication {
    public static void main(String[] args) {
        TestBoneRagdoll app = new TestBoneRagdoll();
        app.setShowSettings(false);
        app.start();
    }

    private AnimControl animControl;
    private AnimChannel animChannel;
    private PhysicsSpace physicsSpace;
    private SphereCollisionShape bulletSphereCollisionShape;
    private Sphere bullet;
    private Node model;
//    private DynamicA

    @Override
    public void simpleInitApp() {
        initLight();
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        bulletAppState.setDebugEnabled(true);

        bullet = new Sphere(32, 32, 1f,true, false);
        bullet.setTextureMode(Sphere.TextureMode.Projected);
        bulletSphereCollisionShape = new SphereCollisionShape(1f);

        model = (Node)assetManager.loadModel("Models/Sinbad/Sinbad.mesh.xml");
        rootNode.attachChild(model);

        animControl = model.getControl(AnimControl.class);
        animChannel = animControl.createChannel();
        animChannel.setAnim("Dance");

        System.out.println(animControl.getAnimationNames());
    }

    private void initLight() {
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-1f,-0.7f,-1f).normalizeLocal());
        directionalLight.setColor(ColorRGBA.LightGray);
        rootNode.addLight(directionalLight);
    }

}
