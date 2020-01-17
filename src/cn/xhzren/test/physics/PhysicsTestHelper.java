package cn.xhzren.test.physics;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class PhysicsTestHelper {

    public static void createPhysicsTestWorld(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);

        Material mat = assetManager.loadMaterial("Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));

        Box floorBox = new Box(100, 100, 2);
        Geometry floorGeometry = new Geometry("floor", floorBox);
        floorGeometry.setMaterial(mat);
        floorGeometry.setLocalTranslation(0, -5, 0);

        floorGeometry.addControl(new RigidBodyControl(2));
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);

        for (int i=0; i< 12;i++) {
            Box box = new Box(0.25f, 0.25f, 0.25f);
            Geometry boxGeometry = new Geometry("Box", box);
            boxGeometry.setMaterial(mat);
            boxGeometry.setLocalTranslation(i, 5, -3);
            boxGeometry.addControl(new RigidBodyControl(2));
            rootNode.attachChild(boxGeometry);
            space.add(boxGeometry);
        }
    }

    /**
     * creates an empty node with a RigidBodyControl
     *
     * @param manager for loading assets
     * @param shape a shape for the collision object
     * @param mass a mass for rigid body
     * @return a new Node
     */
    public static Node createPhysicsTestNode(AssetManager manager, CollisionShape shape, float mass) {
        Node node = new Node("PhysicsNode");
        RigidBodyControl control = new RigidBodyControl(shape, mass);
        node.addControl(control);
        return node;
    }


}
