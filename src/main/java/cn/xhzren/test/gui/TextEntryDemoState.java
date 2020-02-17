package cn.xhzren.test.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.event.PopupState;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.text.DocumentModel;
import com.simsilica.script.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextEntryDemoState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(TextEntryDemoState.class);

    private Container window;

    private TextField textField;
    private DocumentModel document;

    @Override
    protected void initialize(Application app) {

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        window = new Container();
        window.addChild(new Label("Word Wrapped Text", new ElementId("window.title.label")));

        textField = window.addChild(new TextField("input sm"));
        textField.setSingleLine(false);
        document = textField.getDocumentModel();

        textField.setPreferredWidth(300);
        textField.setPreferredLineCount(3);

        Container buttons = window.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y)));
        buttons.addChild(new ActionButton(
                new CallMethodAction(this, "home")));
        buttons.addChild(new ActionButton(
                new CallMethodAction(this, "back")));
        buttons.addChild(new ActionButton(
                new CallMethodAction(this, "forward")));
        buttons.addChild(new ActionButton(
                new CallMethodAction(this, "insert")));
        buttons.addChild(new ActionButton(
                new CallMethodAction(this, "end")));
        buttons.addChild(new ActionButton(
                new CallMethodAction(this, "delete")));

        window.addChild(new ActionButton(new CallMethodAction(this, "close")));
        window.setLocalTranslation(100, 20, 0);
        getState(PopupState.class).showPopup(window);
    }

    @Override
    protected void onDisable() {
        window.removeFromParent();
    }

    public void home() {
        document.home(false);
    }
    public void end() {
        document.end(false);
    }
    public void back() {
        document.left();
    }
    public void insert() {
        document.insert("add");
    }
    public void forward() {
        document.right();
    }
    public void delete() {
        document.delete();
    }

    protected void close() {
        getState(MainMenuState.class).closeChild(this);
    }
}
