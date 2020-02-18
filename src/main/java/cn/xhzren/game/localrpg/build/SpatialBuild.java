/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.game.localrpg.build;

import cn.xhzren.game.localrpg.LocalRpgMain;
import com.jme3.animation.AnimationFactory;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

/**
 *
 * @author admin
 */
public class SpatialBuild {
    private static AssetManager assetManager = LocalRpgMain.assetManagerClose;

    static AnimationFactory factory;

    public static void main(String[] args) {
        Spatial model = assetManager.loadModel("");
    }


    public static Spatial getTextureSpatial(String name) {
        Node node = new Node(name);
        Picture pic = new Picture(name + "_pic");
        Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/"+name+".png");
        pic.setTexture(assetManager,tex,true);
 
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();
        pic.setWidth(width);
        pic.setHeight(height);
        //设置 位置为中心点
        pic.move(-width/2f,-height/2f,0);
 
//        add a material to the picture
        Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        node.setMaterial(picMat);
        node.setUserData("radius", width/2);
        node.attachChild(pic);
        return node;
    }
    
}
