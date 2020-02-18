package cn.xhzren.test.physics;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class TestGhostControl extends SimpleApplication implements ActionListener {

    static Logger log = LoggerFactory.getLogger(TestGhostControl.class);

    public static void main(String[] args) {
        TestGhostControl app = new TestGhostControl();
        app.setShowSettings(false);
        app.start();
    }

    private PhysicsSpace physicsSpace;
    private GhostControl ghostControl;
    private Node capsule;
    @Override
    public void simpleInitApp() {
        BulletAppState bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(true);
        stateManager.attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();

        ghostControl = new GhostControl(new BoxCollisionShape(new Vector3f(1,1,1)));

        Node plane = PhysicsTestHelper.createPhysicsTestNode(assetManager,
                new BoxCollisionShape(new Vector3f(10, 1, 10)),0);
        plane.setLocalTranslation(-5, 0, -5);

        capsule = PhysicsTestHelper.createPhysicsTestNode(assetManager,
                new CapsuleCollisionShape(0.5f, 1),1);
        capsule.setLocalTranslation(0, 4, 0);
        capsule.addControl(ghostControl);


        rootNode.attachChild(plane);
        rootNode.attachChild(capsule);
        physicsSpace.addAll(plane);
        physicsSpace.addAll(capsule);

        inputManager.addMapping("Log", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(this, "Log");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        log.info("overlapping count: {}", capsule.getControl(GhostControl.class).getOverlappingCount());
        for (Iterator<PhysicsCollisionObject> it = capsule.getControl(GhostControl.class).getOverlappingObjects().iterator(); it.hasNext();) {
            log.info("overlapping list: {}", it.next().getUserObject());
        }

    }
}
