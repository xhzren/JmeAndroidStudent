/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.test.terrain;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainQuad;

/**
 *
 * @author admin
 */
public class TestTerrainGrid extends SimpleApplication{
    
    private TerrainGrid terrain;
    private TerrainQuad quad;
    
    public static void main(String[] args) {
        TestTerrainGrid app = new TestTerrainGrid();
        app.start();
    }

    @Override
    public void simpleInitApp() {
         //register assest
        assetManager.registerLocator("TerrainGridTestData.zip", ZipLocator.class);
        
        
        
    }
    
    
    
}
