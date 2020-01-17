package cn.xhzren.game.monkeyzone.control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

public class ManualVehicleControl extends NetworkedManualControl implements PhysicsTickListener {

    private Spatial spatial;
    private VehicleControl vehicleControl;
    private float speed = 800f;
    private float steer = 0;
    private float accelerate = 0;
    private Vector3f tempVec1 = Vector3f.ZERO;
    private Vector3f tempVec2 = Vector3f.ZERO;
    private Vector3f tempVec3 = Vector3f.ZERO;
    private boolean hover = false;
    private boolean added = false;

    public ManualVehicleControl() {
    }

    public ManualVehicleControl(Client client, long entity_id) {
        super(client, entity_id);
    }

    @Override
    public void doSteerX(float amount) {

    }

    @Override
    public void doSteerY(float amount) {

    }

    @Override
    public void doMoveX(float amount) {
        if(!hover) {
            steer = amount * FastMath.QUARTER_PI * 0.5f;
        }else {
            steer = amount;
        }
    }

    @Override
    public void doMoveY(float amount) {

    }

    @Override
    public void doMoveZ(float amount) {
        accelerate = amount * speed;
    }

    @Override
    public void doPerformAction(int action, boolean activate) {

    }

    @Override
    public Vector3f getAimDirection() {
        return vehicleControl.getForwardVector(tempVec1);
    }

    @Override
    public Vector3f getLocation() {
        return vehicleControl.getPhysicsLocation(tempVec2);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        if(spatial == null) {
            if(added) {
                vehicleControl.getPhysicsSpace().removeTickListener(this);
            }
            return;
        }
        this.vehicleControl = spatial.getControl(VehicleControl.class);
        if(this.vehicleControl == null) {
            throw new IllegalStateException("Cannot add ManualCharacterControl to Spatial without CharacterControl");
        }

        Float spatialSpeed = spatial.getUserData("Speed");
        if(spatialSpeed != null) {
            speed = spatialSpeed;
        }

        Integer hoverControl = spatial.getUserData("hoverControl");
        if(hoverControl != null &&hoverControl == 1) {
            hover = true;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void update(float tpf) {
        if(!enabled) {
            return;
        }
        if(hover) {
            if(!added) {
                vehicleControl.getPhysicsSpace().removeTickListener(this);
                added = true;
            }
            return;
        }
        vehicleControl.accelerate(accelerate);
        vehicleControl.steer(steer);
    }

    public void prePhysicsTick(PhysicsSpace space, float f) {
        if(!enabled || !hover) {
            return;
        }
        Vector3f angVel = vehicleControl.getAngularVelocity();
        float rotationVelocity = angVel.getY();
        Vector3f dir = vehicleControl.getForwardVector(tempVec2).multLocal(1,0,1).normalizeLocal();
        vehicleControl.getLinearVelocity(tempVec3);
        Vector3f linearVelocity = tempVec3.multLocal(1, 0,1);

        if(steer != 0) {
            if(rotationVelocity < 1 && rotationVelocity > -1) {
                vehicleControl.applyTorque(tempVec1.set(0, steer * vehicleControl.getMass() * 20, 0));
            }
        }else {
            // counter the steering value!
            if (rotationVelocity > 0.2f) {
                vehicleControl.applyTorque(tempVec1.set(0, -vehicleControl.getMass() * 20, 0));
            } else if (rotationVelocity < -0.2f) {
                vehicleControl.applyTorque(tempVec1.set(0, vehicleControl.getMass() * 20, 0));
            }
        }
        if (accelerate > 0) {
            // counter force that will adjust velocity
            // if we are not going where we want to go.
            // this will prevent "drifting" and thus improve control
            // of the vehicle
            float d = dir.dot(linearVelocity.normalize());
            Vector3f counter = dir.project(linearVelocity).normalizeLocal().negateLocal().multLocal(1 - d);
            vehicleControl.applyForce(counter.multLocal(vehicleControl.getMass() * 10), Vector3f.ZERO);

            if (linearVelocity.length() < 30) {
                vehicleControl.applyForce(dir.multLocal(accelerate), Vector3f.ZERO);
            }
        } else {
            // counter the acceleration value
            if (linearVelocity.length() > FastMath.ZERO_TOLERANCE) {
                linearVelocity.normalizeLocal().negateLocal();
                vehicleControl.applyForce(linearVelocity.mult(vehicleControl.getMass() * 10), Vector3f.ZERO);
            }
        }
    }


    @Override
    public void physicsTick(PhysicsSpace physicsSpace, float v) {

    }

    @Override
    public void render(RenderManager renderManager, ViewPort viewPort) {

    }
}
