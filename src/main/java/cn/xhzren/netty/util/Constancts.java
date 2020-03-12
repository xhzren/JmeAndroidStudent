package cn.xhzren.netty.util;

import java.util.Random;

public class Constancts {

    private static final Random RANDOM = new Random();

    public static final String KEY_MAPPING_JSON = "keyMapping";
    public static final String TASK_HIS_JSON = "taskHis";

    public final static int TOKEN_TIME = 86400 * 15;
    public final static int PORT = 9032;
    public final static float CLIENT_VERSION = 2.0f;
    public final static float SERVER_VERSION = 2.0f;

    public static final String GUI_MATERIALS = "Common/MatDefs/Gui/Gui.j3md";

    private static final String[] TRANSITION_BG = new String[] {
      "",""
    };

    public static String getTransitionBg() {
        return TRANSITION_BG[RANDOM.nextInt(TRANSITION_BG.length - 1)];
    }
}
