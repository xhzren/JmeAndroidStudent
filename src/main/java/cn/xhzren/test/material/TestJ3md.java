package cn.xhzren.test.material;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetKey;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;

import java.awt.*;
import java.util.Random;

public class TestJ3md extends SimpleApplication {

    public static void main(String[] args) {
        TestJ3md app = new TestJ3md();
        app.setShowSettings(false);
        app.start();
    }

    public static final Texture2D defaultTexture = new Texture2D(256, 256, Image.Format.RGBA8);
    static {
        defaultTexture.getImage().setData(BufferUtils.createByteBuffer(256 * 256 * 4));
        ImageRaster raster = ImageRaster.create(defaultTexture.getImage());
        for( int i = 0; i < 256; i++ ) {
            for( int j = 0; j < 256; j++ ) {
                Color hsb = Color.getHSBColor(i/255f, j/255f, 0.5f);
                raster.setPixel(i, j, toJmeColor(hsb));
            }
        }
    }

    protected static ColorRGBA toJmeColor( Color clr ) {
        float r = clr.getRed() /(new Random().nextInt(254) + 1);
        float g = clr.getGreen() / (new Random().nextInt(254) + 1);
        float b = clr.getBlue() / (new Random().nextInt(254) + 1);
        return new ColorRGBA(r, g, b, 1);
    }

    @Override
    public void simpleInitApp() {
        Material material = new Material(assetManager, "Materials/test/Simple.j3md");
        material.setParam("Color", VarType.Vector4, ColorRGBA.Yellow);
        material.setParam("ColorMap", VarType.Texture2D,assetManager.loadTexture("Textures/BumpMapTest/Simple_height.png"));

        Material unshMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        unshMat.setTexture("ColorMap", assetManager.loadTexture("Textures/BumpMapTest/Simple_height.png"));

        Box box = new Box(1, 1, 1);
        Geometry geometry = new Geometry("box");
        geometry.setMesh(box);
        geometry.setMaterial(material);

        Box box2 = new Box(1, 1, 1);
        Geometry geometry2 = new Geometry("box");
        geometry2.setMesh(box2);
        geometry2.setMaterial(unshMat);
        geometry2.setLocalTranslation(3, 0, 0);

        DirectionalLight light = new DirectionalLight();
        Vector3f lightDir=new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        light.setDirection(lightDir);
        light.setColor(ColorRGBA.White.clone().multLocal(2));
        rootNode.addLight(light);

        rootNode.attachChild(geometry);
        rootNode.attachChild(geometry2);
        rootNode.addLight(light);
    }
}
