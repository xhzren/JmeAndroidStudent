package cn.xhzren.game.monkeyzone;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Server;
import com.jme3.system.AppSettings;

public class ZoneServerMain extends SimpleApplication {

    private static Server server;
    private static ZoneServerMain app;

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(Globals.SCENE_FPS);
        settings.setRenderer(null);
        settings.setAudioRenderer(null);

        for (int i = 0; i < args.length; i++) {
            String string = args[i];
            if ("-display".equals(string)) {
                settings.setRenderer(AppSettings.LWJGL_OPENGL2);
            }
        }

        app = new ZoneServerMain();
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {

    }
}
