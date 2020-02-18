package cn.xhzren.test.gems;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.input.Axis;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;


/**
 * 输入映射
 */
public class CameraMovementFunctions {

    public static final String GROUP_MOVEMENT = "Movement";

    public static final FunctionId F_Y_LOCK = new FunctionId(
            GROUP_MOVEMENT, "Y LOOK");
    public static final FunctionId F_X_LOCK = new FunctionId(
            GROUP_MOVEMENT, "X LOOK");

    public static final FunctionId F_MOVE = new FunctionId(
            GROUP_MOVEMENT, "F_MOVE");

    //捕获输入映射
    public static InputMapper.Mapping MOUSE_X_LOOK;
    public static InputMapper.Mapping MOUSE_Y_LOOK;

    public static void initializeDefaultMappings(InputMapper inputMapper) {

        inputMapper.map(F_MOVE, KeyInput.KEY_W);
        //将S设为负数
        inputMapper.map(F_MOVE, InputState.Negative, KeyInput.KEY_S);

        MOUSE_X_LOOK = inputMapper.map(F_X_LOCK, Axis.MOUSE_X);
        inputMapper.map(F_X_LOCK, KeyInput.KEY_RIGHT);
        inputMapper.map(F_X_LOCK,InputState.Negative, KeyInput.KEY_LEFT);

        MOUSE_Y_LOOK = inputMapper.map(F_Y_LOCK, Axis.MOUSE_Y);
        inputMapper.map(F_Y_LOCK, KeyInput.KEY_UP);
        inputMapper.map(F_Y_LOCK,InputState.Negative, KeyInput.KEY_DOWN);
    }
}
