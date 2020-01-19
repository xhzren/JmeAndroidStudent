package cn.xhzren.test.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.style.BaseStyles;
import de.lessvoid.nifty.builder.PanelBuilder;

public class TestLemur extends SimpleApplication {

    public static void main(String[] args) {
        TestLemur app = new TestLemur();
        app.setShowSettings(false);
        app.start();
    }

    private Container window;
    private Label description;
    private VersionedReference<Double> alpha;
    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        GuiGlobals globals = GuiGlobals.getInstance();
        BaseStyles.loadGlassStyle();
//        globals.getStyles().setDefaultStyle("glass");
        window = new Container();
        window.addChild(new Label("Panel Alpha Demo"));

        description = window.addChild(new Label("Drag the slider to change the window alpha:" + 0.5));
        description.setColor(ColorRGBA.Cyan);
        IconComponent icon = new IconComponent("Interface/icons/SmartMonkey32.png");
        icon.setIconScale(0.5f);
        icon.setHAlignment(HAlignment.Center);
        icon.setVAlignment(VAlignment.Top);
        description.setIcon(icon);

        Slider slider = window.addChild(new Slider(new DefaultRangedValueModel(0.1, 0, 0.5)));
        alpha = slider.getModel().createReference();
//        window.addChild(new ActionBu)
        System.out.println(getCamera().getWidth());
        System.out.println(getCamera().getHeight());
        window.setLocalTranslation(getCamera().getWidth()/2, getCamera().getHeight()*0.9f, 50);

        System.out.println(window.getLocalTransform());
        guiNode.attachChild(window);
    }
}
