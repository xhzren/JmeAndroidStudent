package cn.xhzren.netty;

import cn.xhzren.netty.appstates.ItemQueueAppState;
import cn.xhzren.netty.appstates.MapAppState;
import cn.xhzren.netty.controls.CharacterTestControl;
import cn.xhzren.netty.entity.TaskEntity;
import cn.xhzren.netty.entity.TaskRangeEntity;
import cn.xhzren.netty.entity.input.KeyMapping;
import cn.xhzren.netty.entity.input.KeyMappingType;
import cn.xhzren.netty.entity.input.MappingItem;
import cn.xhzren.netty.entity.input.MyKeyInput;
import cn.xhzren.netty.manager.CharacterAppState;
import cn.xhzren.netty.util.JsonUtils;
import cn.xhzren.test.physics.PhysicsTestHelper;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.system.JmeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class SimpleMain extends SimpleApplication implements ActionListener, AnalogListener {

    static Logger logger = LoggerFactory.getLogger(SimpleMain.class);

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

    public static void main(String[] args) {
        SimpleMain app = new SimpleMain();
        app.setShowSettings(false);
        app.start(JmeContext.Type.Headless);
//        app.start();
    }
    AnimChannel walk;
    @Override
    public void simpleInitApp()  {
        JsonUtils.initDefaultLoad();
        initInput();

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.addCollisionListener(new PhysicsCollisionListener() {
            @Override
            public void collision(PhysicsCollisionEvent event) {
                logger.info("A: {},B : {}", event.getNodeA().getName(),
                        event.getNodeB().getName());
                logger.info("type: {}", event.getType());
            }
        });

        world = new Node("world");
        levelNode = new Node("level");
        character = new Node("character");
        jaime = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        jaime.setName("jaime");

        jaime.addControl(new CharacterControl(new SphereCollisionShape(1.6f), 0));
        jaime.setLocalTranslation(Vector3f.ZERO);

        PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, physicsSpace);

        walk = jaime.getControl(AnimControl.class).createChannel();
//      jaime.getControl(AnimControl.class).getAnimationNames().forEach(System.out::println);
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
        physicsSpace.addAll(character);
        physicsSpace.addAll(jaime);

//        walk.setLoopMode(LoopMode.Loop);
//        walk.setAnim("Walk");

        CharacterTestControl characterTestControl = new CharacterTestControl();
        character.addControl(characterTestControl);
        CharacterAppState characterAppState = new CharacterAppState(character);
        getStateManager().attach(characterAppState);

        getStateManager().attach(new ItemQueueAppState());
        getStateManager().attach(new MapAppState());

        rootNode.attachChild(character);
    }


    BufferedReader bufferedReader;
    long start = System.currentTimeMillis();
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

        if(System.currentTimeMillis() - start > 2000) {
            logger.info("box child: {}", rootNode.getChildren().size());
            logger.info("jamie pos: {}", character.getLocalTranslation());
            start = System.currentTimeMillis();
            character.move(1, 0, 0);
        }



//            getStateManager().getState(ItemQueueAppState.class).addTaskEntities(task);
//            getStateManager().getState(MapAppState.class).makeTag("TestChild");
        }

    private void initInput() {
        keyMapping = JsonUtils.keyMappings.stream().filter((e)-> e.isUse())
                .findFirst().get();

        //分为action , analog
        //每个action或者analog 都有一个code列表
//        inputManager.addListener(character.getControl(CharacterTestControl.class)
//                .getActionListener(), keyMapping.getActionMapping().stream().
//                map(e->e.getName()).toArray(String[] :: new));
        inputManager.addListener(this, keyMapping.getActionMapping().stream().
                map(e->e.getName()).toArray(String[] :: new));

//        inputManager.addListener(character.getControl(CharacterTestControl.class)
//                .getAnalogListener(), keyMapping.getAnalogMapping().stream()
//                .map(e->e.getName()).toArray(String[] :: new));
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
}
