package cn.xhzren.game.monkeyzone.control;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.ai.Command;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserCommandControl implements Control, ActionListener {

    protected Screen screen;
    protected TextRenderer[] selectionTexts = new TextRenderer[10];
    protected TextRenderer[] commandTexts = new TextRenderer[10];
    protected List<Class<? extends Command>> commands = new LinkedList<>();
    protected HashMap<Long, Spatial> players = new HashMap<>();
    protected List<Spatial> selectedEntitles = new ArrayList<>();
    protected InputManager inputManager;
    protected HashMap<Integer, LinkedList<Long>> playerGroups = new HashMap<>();
    protected boolean shift = false;
    protected SelectionMenu currentSelectionMenu = SelectionMenu.Main;
    protected WorldManager world;
    protected Spatial userEntity;
    protected boolean enabled = true;

    protected enum SelectionMenu {
        Main,
        Offensive,
        Defensive,
        Builder,
        NavPoints
    }

    public UserCommandControl(Screen screen, InputManager inputManager) {
        this(inputManager);
        this.screen = screen;


    }

    public UserCommandControl(InputManager inputManager) {
        this.inputManager = inputManager;

        //TODO input映射
    }

    public void setWorldManager(WorldManager world) {
        this.world = world;
    }

    public void setPlayerEntity(long id, Spatial entity) {
        if(entity == null) {
            players.remove(id);
            return;
        }

        players.put(id, entity);
        //TODO: apply sphere command type via menu
        //通过菜单应用球体命令类型
    }

    @Override
    public void onAction(String s, boolean b, float v) {

    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }

    @Override
    public void setSpatial(Spatial spatial) {

    }

    @Override
    public void update(float v) {

    }

    @Override
    public void render(RenderManager renderManager, ViewPort viewPort) {

    }

    @Override
    public void write(JmeExporter jmeExporter) throws IOException {

    }

    @Override
    public void read(JmeImporter jmeImporter) throws IOException {

    }
}
