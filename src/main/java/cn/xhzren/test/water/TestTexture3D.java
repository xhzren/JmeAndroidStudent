/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.test.water;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author admin
 */
public class TestTexture3D extends SimpleApplication{
    
    public static void main(String[] args) {
        TestTexture3D app = new TestTexture3D();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Sphere sphere = new Sphere(32, 32, 1);
        System.out.println("befor" + sphere.getBound());
        sphere.updateBound();
        System.out.println("after" + sphere.getBound());
        
        BoundingBox box = (BoundingBox)sphere.getBound();
        //center pos
        Vector3f min = box.getMin(null);
        System.out.println("min after" + min);
        
        float[] ext = new float[]{box.getXExtent() * 2, box.getYExtent() * 2, box.getZExtent() * 2};
        System.out.println("ext" + Arrays.toString(ext));
        sphere.clearBuffer(VertexBuffer.Type.TexCoord);
        
        VertexBuffer vb = sphere.getBuffer(VertexBuffer.Type.Position);
        FloatBuffer vBuffer =(FloatBuffer) vb.getData();
        float[] uvCoordinates = BufferUtils.getFloatArray(vBuffer);
        
        Geometry geometry = new Geometry("sphere", sphere);
        Material material = new Material(assetManager, "/jme3test/texture/tex3D.j3md");
        geometry.setMaterial(material);
        
        
        DirectionalLight sun = Utils.getDirectionalLight();
        
        rootNode.addLight(sun);
        rootNode.attachChild(geometry);
    }
    
}
