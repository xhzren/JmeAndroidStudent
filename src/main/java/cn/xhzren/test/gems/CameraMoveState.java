package cn.xhzren.test.gems;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraMoveState extends BaseAppState
        implements AnalogFunctionListener, StateFunctionListener {

    static Logger log = LoggerFactory.getLogger(CameraMoveState.class);

    private InputMapper inputMapper;
    private Camera camera;
    private double turnSpeed = 2.5;
    private double yam = FastMath.PI;



    @Override
    protected void initialize(Application app) {
        this.camera = app.getCamera();

        if(inputMapper == null) {
            inputMapper = GuiGlobals.getInstance().getInputMapper();
        }

        inputMapper.addAnalogListener(this,
                                        CameraMovementFunctions.F_X_LOCK,
                                        CameraMovementFunctions.F_Y_LOCK,
                                        CameraMovementFunctions.F_MOVE);
    }

    @Override
    public void valueActive(FunctionId func, double value, double tpf) {
        log.info("valueActive: {} value: {}", func, value);
    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        log.info("valueChanged: {} value: {}", func, value);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        inputMapper.activateGroup(CameraMovementFunctions.GROUP_MOVEMENT);
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        getApplication().getInputManager().setCursorVisible(false);
    }

    @Override
    protected void onDisable() {

    }
}
