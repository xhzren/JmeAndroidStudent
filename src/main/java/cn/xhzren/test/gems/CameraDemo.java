package cn.xhzren.test.gems;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.script.CameraMovementState;

public class CameraDemo extends SimpleApplication {

    public static void main(String[] args) {
        CameraDemo app = new CameraDemo();
        app.setShowSettings(false);
        app.start();
    }

    public CameraDemo() {
        super(new StatsAppState(), new CameraMoveState());
    }


    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        CameraMovementFunctions.initializeDefaultMappings(GuiGlobals.getInstance().getInputMapper());

        // Now create the normal simple test scene
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Cyan);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);

    }
}
