package cn.xhzren.game.monkeyzone.control;

import cn.xhzren.game.monkeyzone.Globals;
import cn.xhzren.game.monkeyzone.message.ActionMessage;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import java.awt.*;

/**
 * Manual character control, implements ManualControl interface and controls
 * a character if available on the Spatial.
 * 手动人物控制, 实现手动控制器接口, 并控制Spatial上可用的人物(如果Spatial可用)
 * @author normenhansen
 */
public class ManualCharacterControl extends NetworkedManualControl {

    private Spatial spatial;
    private CharacterControl characterControl;
    private Vector3f walkDirection = Vector3f.ZERO;
    private Vector3f viewDirection = Vector3f.UNIT_Z;
    private Vector3f directionLeft = Vector3f.UNIT_X;
    private Quaternion directionQuat = new Quaternion();
    private Quaternion ROTATE_90 = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
    private float rotAmountX = 0;
    private float rotAmountY = 0;
    private float walkAmount = 0;
    private float strafeAmount = 0;
    private float speed = 10f * Globals.PHYSICS_FPS;
    private Vector3f temVec = new Vector3f();

    public ManualCharacterControl() {
    }

    public ManualCharacterControl(Client client, long entity_id) {
        super(client, entity_id);
    }

    @Override
    public void doSteerX(float amount) {
        rotAmountX = amount;
    }

    @Override
    public void doSteerY(float amount) {
        rotAmountY = amount;
    }

    @Override
    public void doMoveX(float amount) {
        strafeAmount = amount;
    }

    @Override
    public void doMoveY(float amount) {

    }

    @Override
    public void doMoveZ(float amount) {
        walkAmount = amount;
    }

    @Override
    public void doPerformAction(int action, boolean activate) {
        if(activate && action == ActionMessage.JUMP_ACTION) {
            characterControl.jump(Vector3f.UNIT_Y);
        }
    }

    @Override
    public Vector3f getAimDirection() {
        return viewDirection;
    }

    @Override
    public Vector3f getLocation() {
        return characterControl.getPhysicsLocation(temVec);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        if(spatial == null) {
            return;
        }
        this.characterControl = spatial.getControl(CharacterControl.class);
        if(characterControl == null) {
            throw new IllegalStateException("Cannot add ManualCharacterControl to Spatial without CharacterControl");
        }
        Float spatialSpeed = spatial.getUserData("Speed");
        if(spatialSpeed != null) {
            speed = spatialSpeed * Globals.PHYSICS_FPS;
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

        //update if sync changed the directions
        //如果同步改变了方向,则更新
        if(!characterControl.getWalkDirection().equals(walkDirection) || characterControl.getViewDirection().equals(viewDirection)) {
            walkDirection.set(characterControl.getWalkDirection());
            viewDirection.set(characterControl.getViewDirection().normalizeLocal());
            directionLeft.set(viewDirection).normalizeLocal();
            ROTATE_90.multLocal(directionLeft);
        }
        walkDirection.set(viewDirection).multLocal(speed * walkAmount);
        walkDirection.addLocal(directionLeft.mult(speed * strafeAmount));

        if (rotAmountX != 0) {
            //rotate all vectors around the rotation amount
            directionQuat.fromAngleAxis((FastMath.PI) * tpf * rotAmountX, Vector3f.UNIT_Y);
            directionQuat.multLocal(walkDirection);
            directionQuat.multLocal(viewDirection);
            directionQuat.multLocal(directionLeft);
        }
        if (rotAmountY != 0) {
            directionQuat.fromAngleAxis((FastMath.PI) * tpf * rotAmountY, directionLeft);
            directionQuat.multLocal(viewDirection);
            if (viewDirection.getY() > 0.3f || viewDirection.getY() < -0.3f) {
                //rotate all vectors around the rotation amount
                directionQuat.fromAngleAxis((FastMath.PI) * tpf * -rotAmountY, directionLeft);
                directionQuat.multLocal(viewDirection);
            }
        }
        characterControl.setWalkDirection(walkDirection);
        characterControl.setViewDirection(viewDirection);
        //TODO: setting spatial rotation to avoid tilting
        spatial.getLocalRotation().lookAt(temVec.set(viewDirection).multLocal(1, 0, 1), Vector3f.UNIT_Y);
        spatial.setLocalRotation(spatial.getLocalRotation());
    }

    @Override
    public void render(RenderManager renderManager, ViewPort viewPort) {

    }
}
