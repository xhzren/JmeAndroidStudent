package cn.xhzren.test.gui;

import cn.xhzren.avg.Constant;
import cn.xhzren.avg.DialogHelper;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.event.PopupState;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitleDemoState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(DialogDemoState.class);

    private AssetManager assetManager;

    private Container mainWindow;

    @Override
    protected void initialize(Application app) {
        assetManager = app.getAssetManager();

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

        mainWindow = new Container();
        mainWindow.setLocalTranslation(0, Constant.HEIGHT, 0);

        Container buttons = mainWindow.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y)));
        buttons.setBackground(new QuadBackgroundComponent(ColorRGBA.randomColor()));
        buttons.setInsets(new Insets3f(Constant.WIDTH * 0.3f, 20, 10, 10));
        buttons.setLocalScale(1.5f);
        buttons.addChild(new Button("new"), 0).addClickCommands((e)-> {
            DialogHelper.resetDialogData();
            setEnabled(false);
            getState(DialogDemoState.class).setEnabled(true);
            Constant.GAME_STATUS = 2;
        });
        buttons.addChild(new Button("read archive"),1).addClickCommands((e)-> {
            setEnabled(false);
            getState(ArchiveRecordDemoState.class).setEnabled(true);
        });
        buttons.addChild(new Button("tasting"),2).addClickCommands((e)-> {
            log.info("查看解锁场景");
        });
        buttons.addChild(new Button("exit"),3).addClickCommands((e) -> {
            log.info("退出");
        });

        Node gui = ((TestLemur)getApplication()).getGuiNode();
        gui.attachChild(mainWindow);
        GuiGlobals.getInstance().requestFocus(mainWindow);
    }

    @Override
    protected void onDisable() {
        mainWindow.removeFromParent();
    }
}
