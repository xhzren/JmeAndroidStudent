/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.xhzren.test.water;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.savegame.SaveGame;

/**
 *
 * @author admin
 */
public class TestSaveGame extends SimpleApplication{
    
    public static void main(String[] args) {
        TestSaveGame app = new TestSaveGame();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Spatial user = new Node("user");
        
        user.setUserData("name", "xiaoming");
        user.setUserData("sex", "x");
        
        SaveGame.saveGame("/test", "one", user);
    }
    
}
