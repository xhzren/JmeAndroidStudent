package cn.xhzren.test.gui;

import cn.xhzren.avg.gui.DialogDemoState;
import cn.xhzren.avg.gui.TestLemur;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.*;
import com.simsilica.lemur.event.MouseEventControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MainMenuState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(MainMenuState.class);

    public static Class[] DEMOS = {
            IconDemoState.class,
            TextAlignmentDemoState.class,
            ConsoleDemoState.class,
            DialogDemoState.class
    };

    private Container mainWindow;
    private List<ToggleChild> toggles = new ArrayList<>();

    public void closeChild(AppState child ) {
        for( ToggleChild toggle : toggles ) {
            if( toggle.child == child ) {
                toggle.close();
            }
        }
    }

    public float getStandardScale() {
        int height = getApplication().getCamera().getHeight();
        return height/720f;
    }

    protected void showError( String title, String error ) {
        getState(OptionPanelState.class).show(title, error);
    }

    @Override
    protected void initialize(Application app) {
        mainWindow = new Container();
        Label title = mainWindow.addChild(new Label("Test Lemur"));
        title.setFontSize(32);
        title.setInsets(new Insets3f(10,10,0,10));

        Container actions = mainWindow.addChild(new Container());
        actions.setInsets(new Insets3f(10,10,0,10));

        for( Class demo : DEMOS ) {
            ToggleChild toggle = new ToggleChild(demo);
            toggles.add(toggle);
            Checkbox cb = actions.addChild(new Checkbox(toggle.getName()));
            cb.addClickCommands(toggle);
            cb.setInsets(new Insets3f(2, 2, 2, 2));
        }

        ActionButton exit = mainWindow.addChild(new ActionButton(new CallMethodAction("Exit Demo", app, "stop")));
        exit.setInsets(new Insets3f(10,10,0,10));


        // Calculate a standard scale and position from the app's camera
        // height
        int height = app.getCamera().getHeight();
        Vector3f pref = mainWindow.getPreferredSize().clone();

        float standardScale = getStandardScale();
        pref.multLocal(standardScale);

        // With a slight bias toward the top
        float y = height * 0.9f;

        mainWindow.setLocalTranslation(100 * standardScale, y, 0);
        mainWindow.setLocalScale(standardScale);

//        MouseEventControl eventControl = new myMouseClick();
//        mainWindow.addControl(eventControl);
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

    private static String classToName( Class type ) {
        String n = type.getSimpleName();
        if( n.endsWith("DemoState") ) {
            n = n.substring(0, n.length() - "DemoState".length());
        } else if( n.endsWith("State") ) {
            n = n.substring(0, n.length() - "State".length());
        }

        boolean lastLower = false;
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < n.length(); i++ ) {
            char c = n.charAt(i);
            if( lastLower && Character.isUpperCase(c) ) {
                sb.append(" ");
            } else if( Character.isLowerCase(c) ) {
                lastLower = true;
            }
            sb.append(c);
        }
        return sb.toString();
    }


    private class ToggleChild implements Command<Button> {

        private String name;
        private Checkbox check;
        private Class type;
        private AppState child;

        public ToggleChild(Class type) {
            this.type = type;
            this.name = classToName(type);
        }

        public String getName() {
            return name;
        }

        @Override
        public void execute(Button source) {
            this.check = (Checkbox)source;
            log.info("Click: {}", check);
            if(check.isChecked()) {
                open();
            }else {
                close();
            }
        }

        public void open() {
            if(child != null) {
                return;
            }
            try {
                child = (AppState)type.newInstance();
                getStateManager().attach(child);
            }catch (Exception e){
                showError("Error for demo: ", e.toString());
            }
        }

        public void close() {
            if(check != null) {
                check.setChecked(false);
            }
            if(child != null) {
                getStateManager().detach(child);
                child = null;
            }
        }
    }

    private class myMouseClick extends MouseEventControl {
        @Override
        public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture) {
            log.info("mouseEnter: {}",event);
        }

        @Override
        public void mouseExited(MouseMotionEvent event, Spatial target, Spatial capture) {
            log.info("mouseExit: {}", event);
        }

        @Override
        public void mouseMoved(MouseMotionEvent event, Spatial target, Spatial capture) {
//            mainWindow.setLocalTranslation(event.getX(),
//                    event.getY(), 0);
        }

    }
}
