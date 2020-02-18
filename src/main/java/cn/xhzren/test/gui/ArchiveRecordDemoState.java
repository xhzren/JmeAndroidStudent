package cn.xhzren.test.gui;

import cn.xhzren.avg.Constant;
import cn.xhzren.avg.DialogHelper;
import cn.xhzren.avg.entity.ArchiveRecords;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArchiveRecordDemoState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(ArchiveRecordDemoState.class);

    private AssetManager assetManager;

    private Container mainWindow;

    @Override
    protected void initialize(Application app) {
        assetManager = app.getAssetManager();

        mainWindow = new Container();

        mainWindow.addChild(new Label("Archive Record",new ElementId("window.title.label")));
        mainWindow.setPreferredSize(new Vector3f(Constant.WIDTH, Constant.HEIGHT, 0));
        mainWindow.setLocalTranslation(0, Constant.HEIGHT,0);

        Container grids = mainWindow.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y)));
        grids.addChild(new Label(""));

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
