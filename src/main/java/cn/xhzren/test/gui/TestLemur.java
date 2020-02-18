package cn.xhzren.test.gui;

import cn.xhzren.avg.Constant;
import cn.xhzren.avg.DialogHelper;
import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.simsilica.lemur.*;
import com.simsilica.lemur.anim.AnimationState;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.event.MouseAppState;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.style.BaseStyles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLemur extends SimpleApplication {

    static Logger log = LoggerFactory.getLogger(TestLemur.class);


    public static void main(String[] args) {
        DialogHelper.resetDialogData();
        TestLemur app = new TestLemur();
        app.setShowSettings(false);
        app.start();
    }

    public TestLemur() {
        super(new StatsAppState(),
                new DebugKeysAppState(),
                new BasicProfilerState(),
                new BackgroundDemoState(),
                new OptionPanelState(),
                new TitleDemoState(),
//                new DialogDemoState(),
                new AnimationState(),
                new ScreenshotAppState(Constant.screenShot,System.currentTimeMillis()));
    }

    @Override
    public void simpleInitApp() {
        getStateManager().attach(new MouseAppState(this));
        setDisplayStatView(false);
        setDisplayFps(false);
        setPauseOnLostFocus(false);
        GuiGlobals.initialize(this);
        GuiGlobals globals = GuiGlobals.getInstance();
        BaseStyles.loadGlassStyle();
        globals.getStyles().setDefaultStyle("glass");

        Constant.WIDTH = getCamera().getWidth();
        Constant.HEIGHT = getCamera().getHeight();
    }


}
