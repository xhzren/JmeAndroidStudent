package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.physicssync.PhysicsSyncMessage;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * sync message for physics objects (RigidBody + Vehicle)
 * 同步物理对象的消息.
 * @author normenhansen
 */
public class SyncRigidBodyMessage extends PhysicsSyncMessage {

    public Vector3f location;
    public Matrix3f rotation;
    public Vector3f linearVelocity;
    public Vector3f angularVelocity;

    public SyncRigidBodyMessage() {

    }

    public SyncRigidBodyMessage(long id, PhysicsRigidBody body) {
        this.syncId = id;
        location = body.getPhysicsLocation();
        rotation = body.getPhysicsRotationMatrix();

        linearVelocity = new Vector3f();
        body.getLinearVelocity(linearVelocity);
        angularVelocity = new Vector3f();
        body.getAngularVelocity(angularVelocity);
    }

    public void readData(PhysicsRigidBody body) {
        location = body.getPhysicsLocation();
        rotation = body.getPhysicsRotationMatrix();
        linearVelocity = new Vector3f();
        body.getLinearVelocity(linearVelocity);
        angularVelocity = new Vector3f();
        body.getAngularVelocity(angularVelocity);
    }

    @Override
    public void appData(Object body) {
        if(body == null) {
            return;
        }

        PhysicsRigidBody rigidBody = ((Spatial)body).getControl(RigidBodyControl.class);
        if(rigidBody == null) {
            rigidBody = ((Spatial)body).getControl(VehicleControl.class);
        }

        rigidBody.setPhysicsLocation(location);
        rigidBody.setPhysicsRotation(rotation);
        rigidBody.setLinearVelocity(linearVelocity);
        rigidBody.setAngularVelocity(angularVelocity);
    }
}
