package cn.xhzren.netty.appstates;

import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.util.MessageBuild;
import com.jme3.app.Application;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;

import java.util.List;

public class PlayerListAppState extends GuiBaseAppState {

    @Override
    protected void initialize(Application app) {
        super.initialize(app);
        Container recordItem = root.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y)));
        recordItem.addChild(new Label("item" + (1), new ElementId("window.title.label")));
        //get player list
                ConnectionMessage message = MessageBuild.requestInfoBuild(RequestInfo.RequestType.PLAYER_LIST).build();
        getState(ClientAppState.class).channel.writeAndFlush(message);
    }

    public void makePlayerList(PlayerList playerList) {
        logger.info("make list: {}", playerList);
    }


    @Override
    public void update(float tpf) {
        super.update(tpf);
    }
}
