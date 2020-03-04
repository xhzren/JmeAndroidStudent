package cn.xhzren.netty;

import cn.xhzren.netty.controls.CharacterControl;
import cn.xhzren.netty.entity.KeyMapping;
import cn.xhzren.netty.entity.KeyMappingType;
import cn.xhzren.netty.entity.MappingItem;
import cn.xhzren.netty.entity.MyKeyInput;
import cn.xhzren.netty.util.JsonUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class SimpleMain extends SimpleApplication {

    private Node root;
    private Node audioNode;
    private Spatial character;
    private KeyMapping keyMapping;

    public static void main(String[] args) {
        SimpleMain app = new SimpleMain();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {

//        JsonUtils.initDefaultLoad();

        root = new Node("world");

//        character = assetManager.loadModel("");
//        CharacterControl characterControl = new CharacterControl();
//        character.addControl(characterControl);
//        CharacterAppState characterAppState = new CharacterAppState(character);
//        getStateManager().attach(characterAppState);
        initInput();
    }

    private void initInput() {
        keyMapping = JsonUtils.keyMappings.stream().filter((e)-> e.isUse())
                .findFirst().get();

        //分为action , analog
        //每个action或者analog 都有一个code列表
        inputManager.addListener(character.getControl(CharacterControl.class)
                .getActionListener(), keyMapping.getActionMapping().stream().
                map(e->e.getName()).toArray(String[] :: new));
        inputManager.addListener(character.getControl(CharacterControl.class)
                .getAnalogListener(), keyMapping.getAnalogMapping().stream()
                .map(e->e.getName()).toArray(String[] :: new));

        List<MappingItem> sum = new ArrayList<>();
        sum.addAll(keyMapping.getActionMapping());
        sum.addAll(keyMapping.getAnalogMapping());

        List<Object> triggers = new ArrayList<>();
        sum.stream().forEach((e)-> {
            e.getCodes().forEach((key)-> {
                if(KeyMappingType.KEY == key.getType()) {
                    triggers.add(new KeyTrigger(MyKeyInput.valueOf("KEY_SPACE")));
                }
            });
            inputManager.addMapping(e.getName(),triggers.
                    stream().toArray(Trigger[]::new)
            );
        });
    }
}
