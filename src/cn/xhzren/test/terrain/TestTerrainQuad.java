/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.test.terrain;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;

/**
 *
 * @author admin
 */
public class TestTerrainQuad extends SimpleApplication{
    
    private TerrainQuad terrain;
    private Material matRock;
    private Material matWife;
    private BitmapText hintText;
    private boolean isWife = false;
    
    public static void main(String[] args) {
        TestTerrainQuad app = new TestTerrainQuad();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void initialize() {
        super.initialize(); //To change body of generated methods, choose Tools | Templates.
        initGui();
        initInput();
    }
    
    private void initGui() {
        hintText = new BitmapText(guiFont, false);
        hintText.setSize(guiFont.getCharSet().getRenderedSize());
        hintText.setText("Press F is Wafter");
    }
    
    private void initInput() {
        inputManager.addMapping("wireframe", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addListener(actionListener, "wireframe");
        
    }
    
    private ActionListener actionListener = new ActionListener() {
     @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if("wireframe".equals(name) && !isPressed) {
             isWife = !isWife;
            if(isWife) {
                terrain.setMaterial(matWife);
            }else {
                terrain.setMaterial(matRock);
            }
        }
    }       
};
    
    
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50);
        
        //rock
        matRock = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        matRock.setBoolean("useTriPlanarMapping", false);
        matRock.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/quad/alphamap.png"));
        
        Texture dirt = assetManager.loadTexture("Textures/Terrain/quad/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("Tex1", dirt);
        
        Texture grass = assetManager.loadTexture("Textures/Terrain/quad/grass.jpg");
        matRock.setTexture("Tex2", grass);
        
        Texture road = assetManager.loadTexture("Textures/Terrain/quad/road.jpg");
        matRock.setTexture("Tex3", road);
        
        //wife
        matWife = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWife.getAdditionalRenderState().setWireframe(true);
        matWife.setColor("Color", ColorRGBA.Red);
        
        HeightMap heightMap = new ImageBasedHeightMap(
                assetManager.loadTexture("Textures/Terrain/quad/mountains512.png")
                        .getImage(), 1f);
        
        //map quad
        terrain = new TerrainQuad("terrain", 65, 513, heightMap.getHeightMap());
        terrain.setMaterial(matRock);
        //map controller
        TerrainLodControl terrainLodControl = new TerrainLodControl(terrain, getCamera());
        terrain.addControl(terrainLodControl);
        terrain.setLocalTranslation(0, -100, -2);
        
        rootNode.attachChild(terrain);
        
        DirectionalLight light = new DirectionalLight();
        light.setDirection((new Vector3f(-0.5f, -1f, -0.5f)).normalize());
        rootNode.addLight(light);
        
    }
    
}
