package cn.xhzren.test.gui;

import cn.xhzren.avg.DialogHelper;
import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;
import com.simsilica.lemur.anim.AnimationState;
import com.simsilica.lemur.event.MouseAppState;
import com.simsilica.lemur.style.BaseStyles;

public class TestLemur extends SimpleApplication {

    public static void main(String[] args) {
        DialogHelper.init();
        TestLemur app = new TestLemur();
        app.setShowSettings(false);
        app.start();
    }

    private Node log;

    public TestLemur() {

        super(new StatsAppState(), new DebugKeysAppState(),
                new BasicProfilerState(),new OptionPanelState(),
//                new MainMenuState(),
                new DialogDemoState(),
                new AnimationState(),
                new ScreenshotAppState("",System.currentTimeMillis()));
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
    }
}
