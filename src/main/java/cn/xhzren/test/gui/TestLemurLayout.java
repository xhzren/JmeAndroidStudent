package cn.xhzren.test.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.style.BaseStyles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestLemurLayout extends SimpleApplication {

    private static Logger logger = LoggerFactory.getLogger(TestLemurLayout.class);

    public static void main(String[] args) {
        TestLemurLayout app = new TestLemurLayout();
        app.setShowSettings(false);
        app.setDisplayStatView(false);
        app.start();
    }

    private Container window;
    private Button borderLayoutBtn;
    private Button springGridLayoutBtn;
    private Button boxLayoutBtn;

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        GuiGlobals globals = GuiGlobals.getInstance();
        BaseStyles.loadGlassStyle();
        globals.getStyles().setDefaultStyle("glass");
        window = new Container();
        window.setSize(new Vector3f(100,100,0));

        TabbedPanel table = window.addChild(new TabbedPanel());
        Container c1 = new Container();
        borderLayoutBtn = window.addChild(new Button("BorderLayout"));
        borderLayoutBtn.addCommands(Button.ButtonAction.Click,(source -> {
            System.out.println("我被点击了");
        }));
        table.addTab("BorderLayout", borderLayoutBtn);

        guiNode.attachChild(window);

        logger.info("window children: {0}", window.getChildren().size());
    }
}
