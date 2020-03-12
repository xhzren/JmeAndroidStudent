package cn.xhzren.netty.appstates;

import cn.xhzren.avg.Constant;
import cn.xhzren.netty.SimpleMain;
import cn.xhzren.netty.client.DetectVersionClientHandler;
import cn.xhzren.netty.client.LoginClientHandler;
import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.util.Constancts;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import io.netty.channel.Channel;
import sun.security.pkcs11.wrapper.Constants;

import java.util.Random;

public class StartAppState extends GuiBaseAppState {

    private QuadBackgroundComponent bgComponent;
    private String bgPath = Constant.BG_IMAGES[new Random().nextInt(Constant.BG_IMAGES.length - 1)+1];

    private Label loading;


    @Override
    protected void initialize(Application app) {
        super.initialize(app);

        loading = root.addChild(new Label(loadings[0]));
        loading.setLocalTranslation(400, 400, 0);
        loading.setFontSize(32);
        loading.setColor(ColorRGBA.LightGray);

        Texture bg = app.getAssetManager().loadTexture(bgPath);
        bgComponent = new QuadBackgroundComponent(ColorRGBA.randomColor());
        root.setBackground(bgComponent);

        root.setPreferredSize(new Vector3f(app.getCamera().getWidth(), app.getCamera().getHeight(), 0f));
        root.setLocalTranslation(0, app.getCamera().getHeight(), -1);
    }

    public void sendVersion(Channel channel) {
        ConnectionMessage message = ConnectionMessage.
                newBuilder().setDataType(ConnectionMessage.DataType.DetectVersion)
                .setDetectVersion(DetectVersion.newBuilder().
                        setClientVersion(Constancts.CLIENT_VERSION).build()).build();
        channel.writeAndFlush(message);
    }


    String[] loadings = new String[]{
            "loading ..","loading ....","loading ......",
    };
    float tmpIndex = 0;
    @Override
    public void update(float tpf) {
        super.update(tpf);

        tmpIndex+=tpf;
        if(tmpIndex>100000) {
            tmpIndex = 0;
        }
        loading.setText(loadings[(int)tmpIndex%loadings.length]);
    }

    @Override
    protected void cleanup(Application app) {
    }

}
