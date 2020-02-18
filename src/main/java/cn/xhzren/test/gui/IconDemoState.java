package cn.xhzren.test.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.event.PopupState;
import com.simsilica.lemur.style.ElementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IconDemoState extends BaseAppState {
    private static Logger log = LoggerFactory.getLogger(IconDemoState.class);
    private Container window;

    private String[][] icons = new String[][]{
            { "Large","Interface/icons/SmartMonkey128.png"},
            { "Medium","Interface/icons/SmartMonkey32.png"},
            { "Small","Interface/icons/SmartMonkey16.png"}
    };
    private CloseCommand closeCommand = new CloseCommand();
    @Override
    protected void initialize(Application app) {

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        window = new Container();
        window.addChild(new Label("Icon Demo",new ElementId("window.title.label")));

        Container buttonSets = window.addChild(new Container());

        int row = 0;
        int column;

        buttonSets.addChild(new Label("Default"));
        Container buttons = buttonSets.addChild(new Container());
        column = 0;

        for (String[] iconDef : icons) {
            IconComponent icon = new IconComponent(iconDef[1]);
            Button b = buttons.addChild(new Button(iconDef[0]), row,column++);
            b.setIcon(icon);
        }

        buttonSets.addChild(new Label("Default Size, Center Text VAlignment"));
        buttons = buttonSets.addChild(new Container());
        column = 0;

        for (String[] iconDef : icons) {
            IconComponent icon = new IconComponent(iconDef[1]);
            Button b = buttons.addChild(new Button(iconDef[0]), row,column++);
            b.setTextVAlignment(VAlignment.Center);
            b.setIcon(icon);
        }

        // Forced 48x48
        buttonSets.addChild(new Label("Forced Size 48x48, Center Text VAlignment"));
        buttons = buttonSets.addChild(new Container());
        column = 0;
        for( String[] iconDef : icons ) {
            IconComponent icon = new IconComponent(iconDef[1]);
            icon.setIconSize(new Vector2f(48, 48));

            Button b = buttons.addChild(new Button(iconDef[0]), row, column++);
            b.setTextVAlignment(VAlignment.Center);
            b.setIcon(icon);
        }

        // Forced 48x48
        buttonSets.addChild(new Label("Forced 48x48, Icon VAlign Top, All HAlign Center"));
        buttons = buttonSets.addChild(new Container());
        column = 0;
        for( String[] iconDef : icons ) {
            IconComponent icon = new IconComponent(iconDef[1]);
            icon.setIconSize(new Vector2f(48, 48));
            icon.setVAlignment(VAlignment.Top);
            icon.setHAlignment(HAlignment.Center);

            Button b = buttons.addChild(new Button(iconDef[0]), row, column++);
            b.setTextHAlignment(HAlignment.Center);
            b.setIcon(icon);
        }

        // Forced 48x48
        buttonSets.addChild(new Label("Same, icon scale x2"));
        buttons = buttonSets.addChild(new Container());
        column = 0;
        for( String[] iconDef : icons ) {
            IconComponent icon = new IconComponent(iconDef[1]);
            icon.setIconSize(new Vector2f(48, 48));
            icon.setVAlignment(VAlignment.Top);
            icon.setHAlignment(HAlignment.Center);
            icon.setIconScale(2);

            Button b = buttons.addChild(new Button(iconDef[0]), row, column++);
            b.setTextHAlignment(HAlignment.Center);
            b.setIcon(icon);
        }

        window.addChild(new ActionButton(new CallMethodAction("Close",window, "removeFromParent")));
        window.setLocalTranslation(400, getApplication().getCamera().getHeight() * 0.9f, 0);
        getState(PopupState.class).showPopup(window, closeCommand);
    }

    @Override
    protected void onDisable() {
        window.removeFromParent();
    }

    private class CloseCommand implements Command<Object> {
        @Override
        public void execute(Object source) {
            log.info("client iconDemoState");
            getState(MainMenuState.class).closeChild(IconDemoState.this);
        }
    }
}
