package cn.xhzren.test.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.event.PopupState;
import com.simsilica.lemur.style.ElementId;
import groovy.ui.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConsoleDemoState extends BaseAppState {
    static Logger log = LoggerFactory.getLogger(ConsoleDemoState.class);
    private Container window;
    private List<String> hisMsg;

    @Override
    protected void initialize(Application app) {

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        window = new Container();
        window.addChild(new Label("console", new ElementId("window.title.label")));
        Container hisContent = window.addChild(new Container());
        hisContent.addChild(new Label("Microsoft Windows [version 10.0.17763.973]"));
        hisContent.addChild(new Label("(c) 2018 Microsoft Corporation"));

        TextField input = window.addChild(new TextField("input test"));
        input.setFontSize(32);
        input.setColor(ColorRGBA.Blue);
        input.setSingleLine(false);
//      document = input.getDocumentModel();

//      Setup some preferred sizing since this will be the primary
//      element in our GUI
        input.setPreferredWidth(200);
        input.setPreferredLineCount(10);

        window.setLocalTranslation(0,
                getApplication().getCamera().getHeight() * 0.3f, 0);
        getStateManager().getState(PopupState.class).showPopup(window);

    }

    @Override
    protected void onDisable() {
        window.removeFromParent();
    }

}
