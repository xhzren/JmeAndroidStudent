package cn.xhzren.avg.gui;

import cn.xhzren.avg.Constant;
import cn.xhzren.avg.DialogHelper;
import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.simsilica.lemur.*;
import com.simsilica.lemur.anim.AnimationState;
import com.simsilica.lemur.event.MouseAppState;
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
                new PropDemoState(),
                new ArchiveRecordDemoState(),
                new DialogDemoState(),
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
