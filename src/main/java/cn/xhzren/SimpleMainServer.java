package cn.xhzren;

import cn.xhzren.netty.appstates.ClientAppState;
import cn.xhzren.netty.appstates.StartAppState;
import cn.xhzren.netty.controls.CharacterTestControl;
import cn.xhzren.netty.entity.TaskEntity;
import cn.xhzren.netty.entity.TaskRangeEntity;
import cn.xhzren.netty.entity.input.KeyMapping;
import cn.xhzren.netty.entity.input.KeyMappingType;
import cn.xhzren.netty.entity.input.MappingItem;
import cn.xhzren.netty.entity.input.MyKeyInput;
import cn.xhzren.netty.manager.CharacterAppState;
import cn.xhzren.netty.util.JsonUtils;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimpleMainServer extends SimpleApplication implements ActionListener, AnalogListener {

    static Logger logger = LoggerFactory.getLogger(SimpleMainServer.class);

    private Node world;
    private Node audioNode;
    private Node character;
    private Spatial jaime;
    private Node levelNode;
    private KeyMapping keyMapping;

    private BulletAppState bulletAppState;
    private PhysicsSpace physicsSpace;
    private CollisionShape shape;

    private Quad panel;

    private Channel channel;

    public static void main(String[] args) throws Exception {

        SimpleMainServer app = new SimpleMainServer();
        app.setShowSettings(false);
//        app.start(JmeContext.Type.Headless);
        app.start();
    }

    public SimpleMainServer(Channel channel) {
        this.channel = channel;
    }
    public SimpleMainServer() {

    }

    AnimChannel walk;
    @Override
    public void simpleInitApp()  {
        JsonUtils.initDefaultLoad();
        initInput();

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();

        world = new Node("world");
        levelNode = new Node("level");
        character = new Node("character");

        jaime = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        jaime.setName("jaime");
        walk = jaime.getControl(AnimControl.class).createChannel();
        jaime.getControl(AnimControl.class).addListener(new AnimEventListener() {
            @Override
            public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
                logger.info("{} is run", animName);
                }

            @Override
            public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
                logger.info("anim change, {}", animName);
            }
        });
        character.attachChild(jaime);
        character.addControl(new CharacterTestControl());
        CharacterAppState characterAppState = new CharacterAppState(character);
        getStateManager().attach(characterAppState);


//        getStateManager().attach(new ItemQueueAppState());
//        getStateManager().attach(new MapAppState());
//        getStateManager().attach(new TransitionSceneAppState());
        getStateManager().attach(new StartAppState());
        getStateManager().attach(new ClientAppState());

        rootNode.attachChild(character);

        setDisplayStatView(false);
        setDisplayFps(false);
        setPauseOnLostFocus(false);
        GuiGlobals.initialize(this);
        GuiGlobals globals = GuiGlobals.getInstance();
        BaseStyles.loadGlassStyle();
        globals.getStyles().setDefaultStyle("glass");
    }


    long start = System.currentTimeMillis();
    boolean is = false;
    @Override
    public void simpleUpdate(float tpf) {
            TaskEntity task = new TaskEntity();
            TaskRangeEntity taskRange = new TaskRangeEntity();
            taskRange.setTop(new Vector2f(30, 30));
            taskRange.setDown(new Vector2f(30, 0));
            taskRange.setLeft(new Vector2f(10, 10));
            taskRange.setTop(new Vector2f(40, 40));
            List<TaskRangeEntity> rangeEntities = new ArrayList<>();
            rangeEntities.add(taskRange);
            task.setCurrentTag(true);
            task.setRangeEntities(rangeEntities);

//          getStateManager().getState(ItemQueueAppState.class).addTaskEntities(task);
//          getStateManager().getState(MapAppState.class).makeTag("TestChild");
        }

    private void initInput() {
        keyMapping = JsonUtils.keyMappings.stream().filter((e)-> e.isUse())
                .findFirst().get();

        //分为action , analog
        //每个action或者analog 都有一个code列表
        inputManager.addListener(this, keyMapping.getActionMapping().stream().
                map(e->e.getName()).toArray(String[] :: new));

        inputManager.addListener(this, keyMapping.getAnalogMapping().stream()
                .map(e->e.getName()).toArray(String[] :: new));


        List<MappingItem> sum = new ArrayList<>();
        sum.addAll(keyMapping.getActionMapping());
        sum.addAll(keyMapping.getAnalogMapping());

        sum.stream().forEach((e)-> {
            List<Object> triggers = new ArrayList<>();
            e.getCodes().forEach((key)-> {
                if(KeyMappingType.KEY == key.getType()) {
                    triggers.add(new KeyTrigger(MyKeyInput.valueOf(key.getCode())));
                }
            });
            inputManager.addMapping(e.getName(),triggers.
                    stream().toArray(Trigger[]::new)
            );
        });
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        logger.info("action : name-> {},isPressed-> {}", name, isPressed);
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        logger.info("analog : name-> {}, time-> {}", name, value);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
