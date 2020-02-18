package cn.xhzren.test.camera;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.Cinematic;

public class TestCinematic extends SimpleApplication {


    private Cinematic cinematic;

    @Override
    public void simpleInitApp() {
        cinematic = new Cinematic(rootNode);
    }
}
