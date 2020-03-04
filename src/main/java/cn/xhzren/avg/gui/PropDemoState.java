package cn.xhzren.avg.gui;

import cn.xhzren.avg.Constant;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropDemoState extends BaseAppState {
    static Logger log = LoggerFactory.getLogger(PropDemoState.class);

    private Container mainWindow;
    private Command sureCommand;
    private String title;

    @Override
    protected void initialize(Application app) {
//        setEnabled(false);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        mainWindow = new Container();
        mainWindow.setPreferredSize(new Vector3f(Constant.WIDTH * 0.3f,
                Constant.HEIGHT * 0.2f, 0));
        mainWindow.setLocalTranslation(Constant.WIDTH * 0.5f,
                Constant.HEIGHT * 0.5f, 0);
        Label titleLabel = mainWindow.addChild(new Label(title));
        titleLabel.setTextHAlignment(HAlignment.Center);
        titleLabel.setTextVAlignment(VAlignment.Center);

        Container btns = mainWindow.addChild(new Container(new SpringGridLayout(Axis.Y,Axis.X, FillMode.None,FillMode.None)));
        Button sureBtn = btns.addChild(new Button("sure"),0,0);
        sureBtn.addClickCommands(sureCommand);
        sureBtn.setLocalScale(1.5f);
        sureBtn.setInsets(new Insets3f(10, 10, 10, 10));

        Button cancelBtn = btns.addChild(new Button("cancel"), 0,1);
        cancelBtn.addClickCommands((e) -> {
            log.info("cancel");
            mainWindow.removeFromParent();
        });
        cancelBtn.setLocalScale(1.5f);
        cancelBtn.setInsets(new Insets3f(10, 10, 10, 10));

        Node gui = ((TestLemur) getApplication()).getGuiNode();
        gui.attachChild(mainWindow);
        GuiGlobals.getInstance().requestFocus(mainWindow);
    }

    @Override
    protected void onDisable() {
        if(mainWindow != null) {
            mainWindow.removeFromParent();
        }
        sureCommand = null;
        title = null;
    }

    public PropDemoState setSureCommand(Command sureCommand) {
        this.sureCommand = sureCommand;
        return this;
    }

    public PropDemoState setTitle(String title) {
        this.title = title;
        return this;
    }

    public Container getMainWindow() {
        return mainWindow;
    }
}
