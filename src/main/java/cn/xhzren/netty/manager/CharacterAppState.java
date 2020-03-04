package cn.xhzren.netty.manager;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.List;

public class CharacterAppState extends BaseAppState {

    private Node character;

    public CharacterAppState(Spatial character) {
        this.character = new Node();
        this.character.attachChild(character);
    }

    @Override
    protected void initialize(Application app) {

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
