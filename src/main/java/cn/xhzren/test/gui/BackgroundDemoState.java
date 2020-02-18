package cn.xhzren.test.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.util.Screenshots;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackgroundDemoState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(BackgroundDemoState.class);

    private AssetManager assetManager;

    private Container mainWindow;
    private String bgPath = "Textures/Avg/bg_1.jpg";

    @Override
    protected void initialize(Application app) {
        assetManager = app.getAssetManager();
        mainWindow = new Container();
        Texture bg = app.getAssetManager().loadTexture(bgPath);
        mainWindow.setBackground(new QuadBackgroundComponent(bg));

        mainWindow.setPreferredSize(new Vector3f(app.getCamera().getWidth(), app.getCamera().getHeight(), 0f));
        mainWindow.setLocalTranslation(0, app.getCamera().getHeight(), -1);

        ScreenshotAppState state = getState(ScreenshotAppState.class);
        state.takeScreenshot();
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        Node gui = ((TestLemur)getApplication()).getGuiNode();
        gui.attachChild(mainWindow);
        GuiGlobals.getInstance().requestFocus(mainWindow);
    }

    @Override
    protected void onDisable() {
        mainWindow.removeFromParent();
    }
}
