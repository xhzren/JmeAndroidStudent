package cn.xhzren.game.monkeyzone.control;

import cn.xhzren.game.monkeyzone.Globals;
import cn.xhzren.game.monkeyzone.message.ActionMessage;
import com.jme3.app.SimpleApplication;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * When attached to a Spatial, searches for ManualControl and sends user
 * input there, only used on client for current user entity.
 * @author normenhansen
 * 附加到Spatial后, 搜索ManualControl并将用户输入发送到该空间, 仅在客户端上
 * 用于当前用户实体.
 */
public class UserInputControl implements Control, ActionListener, AnalogListener {
    //TODO: add support for joysticks, mouse axis etc. and localization
    //添加对手柄, 鼠标轴的支持, 本地化

    private InputManager inputManager;
    private Spatial spatial = null;
    private ManualControl manualControl = null;
    private boolean enabled = true;
    private float moveX = 0;
    private float moveY = 0;
    private float moveZ = 0;
    private float steerX = 0;
    private float steerY = 0;
    private Camera camera;

    public UserInputControl(InputManager inputManager, Camera camera) {
        this.inputManager = inputManager;
        this.camera = camera;

        prepareInputManager();
    }

    private void prepareInputManager() {
        inputManager.addMapping(Globals.UserInput_Left_Key,
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(Globals.UserInput_Right_Key,
                new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(Globals.UserInput_Down_Key,
                new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(Globals.UserInput_Up_Key,
                new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(Globals.UserInput_Left_Arrow_Key,
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(Globals.UserInput_Right_Arrow_Key,
                new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(Globals.UserInput_Space_Key,
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping(Globals.UserInput_Enter_Key,
                new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping(Globals.UserInput_Left_Mouse,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(Globals.UserInput_Mouse_Axis_X_Left,
                new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(Globals.UserInput_Mouse_Axis_X_Right,
                new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(Globals.UserInput_Mouse_Axis_Y_Down,
                new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(Globals.UserInput_Mouse_Axis_Y_Up,
                new MouseAxisTrigger(MouseInput.AXIS_Y, false));

        inputManager.addListener(this,
                "UserInput_Left_Key",
                "UserInput_Right_Key",
                "UserInput_Up_Key",
                "UserInput_Down_Key",
                "UserInput_Left_Arrow_Key",
                "UserInput_Right_Arrow_Key",
                "UserInput_Space_Key",
                "UserInput_Enter_Key",
                "UserInput_Left_Mouse",
                "UserInput_Mouse_Axis_X_Left",
                "UserInput_Mouse_Axis_X_Right",
                "UserInput_Mouse_Axis_Y_Up",
                "UserInput_Mouse_Axis_Y_Down");
    }


    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
        if(spatial == null) {
            manualControl = null;
            return;
        }

        manualControl = spatial.getControl(ManualControl.class);
        if(manualControl == null) {
            throw new IllegalStateException("Cannot add UserInputControl to spatial without ManualControl!");
        }
    }

    @Override
    public void update(float tpf) {
        if(steerX != 0) {
            steerX = 0;
        }else {
            manualControl.steerX(steerX);
        }

        if(steerY!= 0) {
            steerY = 0;
        }else {
            manualControl.steerY(steerY);
        }

        //TODO: replace with special spatial
        //转换成特定的空间
        Vector3f currentUp = spatial.getWorldRotation().mult(Vector3f.UNIT_X);
        Vector3f camLocation = spatial.getWorldTranslation().add(currentUp);
        camera.setLocation(camLocation);
        camera.setRotation(spatial.getWorldRotation());
        camera.lookAt(camLocation.addLocal(manualControl.getAimDirection()),spatial.getWorldRotation().mult(Vector3f.UNIT_X));
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if(!isEnabled() || manualControl == null) {
            return;
        }

        if(Globals.UserInput_Left_Key.equals(binding)) {
            if(value) {
                moveX += 1;
            }else {
                moveX -= 1;
            }
            manualControl.moveX(moveX);
        }else if(Globals.UserInput_Right_Key.equals(binding)) {
            if(value) {
                moveX -= 1;
            }else {
                moveX += 1;
            }
            manualControl.moveX(moveX);
        }else if(Globals.UserInput_Up_Key.equals(binding)) {
            if(value) {
                moveZ += 1;
            }else {
                moveZ -= 1;
            }
            manualControl.moveZ(moveZ);
        }else if(Globals.UserInput_Down_Key.equals(binding)) {
            if(value) {
                moveZ -= 1;
            }else {
                moveZ += 1;
            }
            manualControl.moveZ(moveZ);
        }else if(Globals.UserInput_Space_Key.equals(binding)) {
            manualControl.performAction(ActionMessage.JUMP_ACTION, value);
        }else if(Globals.UserInput_Enter_Key.equals(binding)) {
            manualControl.performAction(ActionMessage.ENTER_ACTION, value);
        }else if(Globals.UserInput_Left_Mouse.equals(binding)) {
            manualControl.performAction(ActionMessage.SHOOT_ACTION, value);
        }
    }

    @Override
    public void onAnalog(String binding, float value, float tpf) {
        if(!isEnabled() || manualControl == null) {
            return;
        }
        if(Globals.UserInput_Mouse_Axis_X_Left.equals(binding)) {
            steerX = value / tpf;
            steerX = steerX > 1 ? 1 :steerX;
            manualControl.steerX(steerX);
        }else if(Globals.UserInput_Mouse_Axis_X_Right.equals(binding)) {
            steerX = value / tpf;
            steerX = steerX > 1 ? 1 :steerX;
            manualControl.steerX(-steerX);
        }else if(Globals.UserInput_Mouse_Axis_Y_Up.equals(binding)) {
            steerY = value / tpf;
            steerY = steerY > 1 ? 1 :steerY;
            manualControl.steerY(steerY);
        }else if(Globals.UserInput_Mouse_Axis_Y_Down.equals(binding)) {
            steerY = value / tpf;
            steerY = steerY > 1 ? 1 :steerY;
            manualControl.steerY(-steerY);
        }
    }


    @Override
    public void render(RenderManager renderManager, ViewPort viewPort) {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void write(JmeExporter jmeExporter) throws IOException {

    }

    @Override
    public void read(JmeImporter jmeImporter) throws IOException {

    }
}
