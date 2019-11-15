package cn.xhzren.game.monkeyzone;

/**
 * 常量配置类
 */
public class Globals {

    public static final String VERSION = "ZoneCopy v0.1";
    public static final String DEFAULT_SERVER = "127.0.0.1";
    public static final int PROTOCOL_VERSION = 1;
    public static final int CLIENT_VERSION = 1;
    public static final int SERVER_VERSION = 1;

    //网络同步频率
    public static final float NETWORK_SYNC_FREQUENCY = 0.25f;
    //物理延迟
    public static final float NETWORK_MAX_PHYSICS_DELAY = 0.25f;
    //画面FPS
    public static final int SCENE_FPS = 60;
    //物理FPS
    public static final float PHYSICS_FPS = 60;
    //
    public static final boolean PHYSICS_THREADED = true;
    public static final boolean PHYSICS_DEBUG = false;
    public static final int DEFAULT_PORT_TCP = 6143;
    public static final int DEFAULT_PORT_UDP = 6143;

    /**
     * Input
     */
    public static final String UserInput_Left_Key = "UserInput_Left_Key";
    public static final String UserInput_Right_Key = "UserInput_Right_Key";
    public static final String UserInput_Up_Key = "UserInput_Up_Key";
    public static final String UserInput_Down_Key = "UserInput_Down_Key";
    public static final String UserInput_Left_Arrow_Key = "UserInput_Left_Arrow_Key";
    public static final String UserInput_Right_Arrow_Key = "UserInput_Right_Arrow_Key";
    public static final String UserInput_Space_Key = "UserInput_Space_Key";
    public static final String UserInput_Enter_Key = "UserInput_Enter_Key";
    public static final String UserInput_Left_Mouse = "UserInput_Left_Mouse";
    public static final String UserInput_Mouse_Axis_X_Left = "UserInput_Mouse_Axis_X_Left";
    public static final String UserInput_Mouse_Axis_X_Right = "UserInput_Mouse_Axis_X_Right";
    public static final String UserInput_Mouse_Axis_Y_Up = "UserInput_Mouse_Axis_Y_Up";
    public static final String UserInput_Mouse_Axis_Y_Down = "UserInput_Mouse_Axis_Y_Down";

}
