package cn.xhzren.test.physics;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class PhysicsTest extends SimpleApplication {

    private BulletAppState bulletAppState;

    public static void main(String[] args) {
        PhysicsTest app = new PhysicsTest();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);

        Node physicsSphere = PhysicsTestHelper.createPhysicsTestNode(assetManager,
                new SphereCollisionShape(1), 1);
        physicsSphere.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0, 3, 0));
        rootNode.attachChild(physicsSphere);
        bulletAppState.getPhysicsSpace().add(physicsSphere);

        Node node2 = PhysicsTestHelper.createPhysicsTestNode(assetManager, new MeshCollisionShape(new Sphere(16, 16, 1.2f)), 0);
        node2.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(2.5f, -4, 0f));
        rootNode.attachChild(node2);
        bulletAppState.getPhysicsSpace().add(node2);

        Node node3 = PhysicsTestHelper.createPhysicsTestNode(assetManager,
                new PlaneCollisionShape(new Plane(new Vector3f(0,1,0),0)), 0);
        node3.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0, -2, 0));
        rootNode.attachChild(node3);
        bulletAppState.getPhysicsSpace().add(node3);

    }

    @Override
    public void update() {

    }
}
