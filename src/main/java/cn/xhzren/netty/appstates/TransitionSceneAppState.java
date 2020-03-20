package cn.xhzren.netty.appstates;

import cn.xhzren.avg.Constant;
import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import com.jme3.app.Application;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import com.jme3.texture.Texture;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.PopupState;
import com.simsilica.lemur.style.ElementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class TransitionSceneAppState extends GuiBaseAppState {

    static Logger logger = LoggerFactory.getLogger(TransitionSceneAppState.class);

    private QuadBackgroundComponent bgComponent;
    private ProgressBar progressBar;
    private String bgPath = Constant.BG_IMAGES[new Random().nextInt(Constant.BG_IMAGES.length - 1)+1];

    private Line line;

    Material shapeMat;
    @Override
    protected void initialize(Application app) {
        super.initialize(app);
//        bg = application.getAssetManager().loadTexture(Constancts.getTransitionBg());
        line = new Line(new Vector3f(100,30,0), new Vector3f(200,30,0));
        Geometry shapeGeo = new Geometry("propress", line);
       shapeMat = new Material(app.getAssetManager(),"Common/MatDefs/Misc/ShowNormals.j3md");
        shapeGeo.setMaterial(shapeMat);

        root.addChild(new Label("username"));
        TextField username = root.addChild(new TextField(""));
        root.addChild(new Label("password"));
        TextField password = root.addChild(new TextField(""));
        root.addChild(new Button("")).addClickCommands((e)-> {
            logger.info("login");
            //local find sql data
             //server exchange character data
            ConnectionMessage login = ConnectionMessage.newBuilder()
                            .setDataType(DataType.LoginType)
                            .setLogin(Login.newBuilder().setId(1)
                                    .setLoginType(Login.LoginType.SELF)
                                    .setName(username.getText()).setPassWord(password.getText()).build()).build();
            getState(ClientAppState.class).channel.writeAndFlush(login);
        });


        Texture bg = app.getAssetManager().loadTexture(bgPath);
        bgComponent = new QuadBackgroundComponent(ColorRGBA.randomColor());
//        mainWindow.setBackground(bgComponent);


        root.setPreferredSize(new Vector3f(app.getCamera().getWidth(), app.getCamera().getHeight(), 0f));
        root.setLocalTranslation(0, app.getCamera().getHeight(), -1);
        root.attachChild(root);
//        root.attachChild(shapeGeo);
    }

    public void popupTips(String context) {
        Container tips = new Container();
        tips.setPreferredSize(new Vector3f(getApplication().getCamera().getWidth(),
                getApplication().getCamera().getHeight(),0));
        tips.setLocalTranslation(0,
                getApplication().getCamera().getHeight(), 0);
        tips.addChild(new Label("Title",new ElementId("window.title.label")));

        Label content = tips.addChild(new Label(context));
        content.setFontSize(40);
        content.setLocalTranslation(tips.getPreferredSize().x/2,tips.getPreferredSize().y/2 , 0);
        tips.addMouseListener(new DefaultMouseListener(){
            @Override
            protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
                tips.removeFromParent();
            }
        });

        getState(PopupState.class).showModalPopup(null);
    }

    @Override
    public void update(float tpf) {
       super.update(tpf);
    }

}
