package cn.xhzren.test.gui;

import cn.xhzren.avg.DialogHelper;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.event.PopupState;
import com.simsilica.lemur.style.ElementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitleDemoState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(DialogDemoState.class);

    private AssetManager assetManager;

    private Container mainWindow;

    @Override
    protected void initialize(Application app) {
        assetManager = app.getAssetManager();

        mainWindow = new Container();
//        mainWindow.setBackground(new QuadBackgroundComponent(ColorRGBA.randomColor()));

//        mainWindow.setSize(new Vector3f(500, 500, 0f));
        mainWindow.setLocalTranslation(0, app.getCamera().getHeight(), 0);

        Container buttons = mainWindow.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y)));
        buttons.setBackground(new QuadBackgroundComponent(ColorRGBA.randomColor()));
        buttons.setInsets(new Insets3f(app.getCamera().getHeight() * 0.3f, 20, 10, 10));
        buttons.setLocalScale(1.5f);
        buttons.addChild(new Button("new"), 0).addClickCommands((e)-> {
            DialogHelper.resetDialogData();
            setEnabled(false);
            getStateManager().attach(new DialogDemoState());
        });
        buttons.addChild(new Button("read archive"),1).addClickCommands((e)-> {
            log.info("读取存档");

        });
        buttons.addChild(new Button("tasting"),2).addClickCommands((e)-> {
            log.info("查看解锁场景");
        });
        buttons.addChild(new Button("exit"),3).addClickCommands((e) -> {
            log.info("退出");
        });

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
}
