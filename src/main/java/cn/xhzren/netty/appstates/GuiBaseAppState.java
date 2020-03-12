package cn.xhzren.netty.appstates;

import cn.xhzren.netty.SimpleMain;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.lemur.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiBaseAppState extends BaseAppState {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    protected SimpleMain app;

    protected Container root;

    public boolean isClose = false;

    @Override
    protected void initialize(Application app) {
        this.app = (SimpleMain)app;
        root = new Container();
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    public void update(float tpf) {
        if(isClose) {
            onDisable();
            getStateManager().detach(this);
            return;
        }
    }

    @Override
    protected void onEnable() {
        app.getGuiNode().attachChild(root);
    }

    @Override
    protected void onDisable() {
        root.removeFromParent();
    }
}
