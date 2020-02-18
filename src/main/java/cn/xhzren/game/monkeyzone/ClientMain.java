package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.control.DefaultHUDControl;
import cn.xhzren.game.monkeyzone.control.UserCommandControl;
import cn.xhzren.game.monkeyzone.control.UserInputControl;
import cn.xhzren.game.monkeyzone.data.PlayerData;
import cn.xhzren.game.monkeyzone.message.*;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.NetworkClient;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.TextCreator;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The client Main class, also the screen controller for most parts of the
 * login and lobby GUI
 * 客户端Main类, 也是大多数登录和大厅GUI的屏幕控制器
 * @author normenhansen
 */
public class ClientMain extends SimpleApplication implements ScreenController {

    private static ClientMain app;

    public static void main(String[] args) {
        app = new ClientMain();
        for (int i = 0; i < args.length; i++) {
            String string = args[i];
            if ("-server".equals(string)) {
                ServerMain.main(args);
                return;
            }
        }

        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(Globals.SCENE_FPS);
        settings.setSettingsDialogImage("/Interface/Images/splash-small.jpg");
        settings.setTitle("MonkeyZone");
        Util.registerSerializers();
        Util.setLogLevels(true);
        app = new ClientMain();
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    private WorldManager worldManager;
    private PhysicsSyncManager syncManager;
    private ClientEffectsManager effectsManager;
    private UserCommandControl commandControl;
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    private TextRenderer statusText;
    private NetworkClient client;
    private ClientNetListener listenerManager;
    private BulletAppState bulletAppState;

    @Override
    public void simpleInitApp() {
        startNifty();
        client = Network.createClient();
        bulletAppState = new BulletAppState();
        if(Globals.PHYSICS_THREADED) {
            bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        }
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setAccuracy(Globals.PHYSICS_FPS);
        bulletAppState.setDebugEnabled(Globals.PHYSICS_DEBUG);
        inputManager.setCursorVisible(false);
        flyCam.setEnabled(false);

        syncManager = new PhysicsSyncManager(client, app);
        syncManager.setMaxDelay(Globals.NETWORK_MAX_PHYSICS_DELAY);
        syncManager.setMessageTypes(AutoControlMessage.class,
                ManualControlMessage.class,
                ActionMessage.class,
                SyncCharacterMessage.class,
                SyncRigidBodyMessage.class,
                ServerEntityDataMessage.class,
                ServerEnterEntityMessage.class,
                ServerAddEntityMessage.class,
                ServerAddPlayerMessage.class,
                ServerEffectMessage.class,
                ServerEnableEntityMessage.class,
                ServerDisableEntityMessage.class,
                ServerRemoveEntityMessage.class,
                ServerRemovePlayerMessage.class);
        stateManager.attach(syncManager);

        //ai manager for controlling units
        commandControl = new UserCommandControl(nifty.getScreen("default_hud"), inputManager);
        //world manager, manages entites and server commands
        worldManager = new WorldManager(this, rootNode, commandControl);
        //adding/creating controls later attached to user controlled spatial
        worldManager.addUserControl(new UserInputControl(inputManager, cam));
        worldManager.addUserControl(commandControl);
        worldManager.addUserControl(new DefaultHUDControl(nifty.getScreen("default_hud")));
        stateManager.attach(worldManager);
        //effects manager for playing effect
        effectsManager = new ClientEffectsManager();
        stateManager.attach(effectsManager);
        //register effects manager and world manager with sync manager so that messages can apply their data
        syncManager.addObject(-2, effectsManager);
        syncManager.addObject(-1, worldManager);

        listenerManager = new ClientNetListener(app, client, worldManager);
    }

    @Override
    public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen) {

    }

    @Override
    public void onStartScreen() {

    }

    @Override
    public void onEndScreen() {

    }

    /**
     * starts the nifty gui system
     * 启动nifty gui系统
     */
    private void startNifty() {
        guiNode.detachAllChildren();
        guiNode.attachChild(fpsText);
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager,
                audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        try {
            nifty.fromXml("Interface/ClientUI.xml",
                    "load_game", this);
        }catch (Exception e) {
            e.printStackTrace();
        }
        statusText = nifty.getScreen("load_game").findElementById("layer").findElementById("panel").findElementById("status_text").getRenderer(TextRenderer.class);
        guiViewPort.addProcessor(niftyDisplay);
    }

    @Override
    public void destroy() {
        try {
            client.close();
        }catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    null, e);
        }
        super.destroy();
    }

    /**
     * add text to chat window, threadsafe
     * 向聊天窗口添加文本， 线程安全
     * @param text
     */
    public void addChat(String text) {
        enqueue(() -> {
            Screen screen = nifty.getScreen("lobby");
            Element panel = screen.findElementById("layer").findElementById("bottom_panel").findElementById("chat_panel").findElementById("chat_list").findElementById("chat_list_panel");
            TextCreator labelCreator = new TextCreator(text);
            labelCreator.setStyle("label chat");
            labelCreator.create(nifty, screen, panel);
            return null;
        });
    }

    public void sendMessage(String text) {
        client.send(new ChatMessage(text));
    }

    /**
     * send message to start selected game
     * 发送消息以开始选择的游戏
     */
    public void startGame() {
        //TODO: map selection
        client.send(new StartGameMessage("Scenes/MonkeyZone.j3o"));
    }

    /**
     * connect to server (called from gui)
     * 连接到服务器
     */
    public void connect() {
        //TODO: not connect when already trying..
        final String userName = nifty.getScreen("load_game").findElementById("layer").findElementById("panel").findElementById("username_text").getControl(TextFieldControl.class).getText();
        if(userName.trim().length() == 0) {
            setStatusText("Username invalid");
            return;
        }
        listenerManager.setName(userName);
        statusText.setText("Connecting...");
        try {
            client.connectToServer(Globals.DEFAULT_SERVER, Globals.DEFAULT_PORT_TCP, Globals.DEFAULT_PORT_UDP);
            client.start();
        } catch (IOException ex) {
            setStatusText(ex.getMessage());
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * updates the list of players in the lobby gui, threadsafe
     * 更新游戏大厅GUI显示的玩家列表， 线程安全
     */
    public void updatePlayerData() {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "Updating player data");
        enqueue(() -> {
            Screen screen = nifty.getScreen("lobby");
            Element panel = screen.findElementById("layer").findElementById("panel").findElementById("players_panel").findElementById("players_list").findElementById("panel");
            List<PlayerData> players = PlayerData.getHumanPlayers();
            for (Iterator<Element> it = panel.getChildren().iterator();it.hasNext();) {
                Element element = it.next();
                element.markForRemoval();
            }
            TextCreator labelCreator = new TextCreator("unknown player");
            labelCreator.setStyle("my-listbox-item-style");
            for (Iterator<PlayerData> it = players.iterator();it.hasNext();) {
                PlayerData playerData = it.next();
                Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                        "Player list {0}", playerData);
                labelCreator.setText(playerData.getStringData("name"));
                labelCreator.create(nifty, screen, panel);
            }
            return null;
        });
    }

    /**
     * loads a level, basically does everything on a seprate thread except
     * updating the UI and attaching the level
     * 加载一个关卡， 基本上在一个单独的线程上执行所有操作，
     * 除了更新UI和将关卡加到场景中
     * @param name
     * @param modelNames
     */
    public void loadLevel(final String name, final String[] modelNames) {
        final TextRenderer statusText = nifty.getScreen("load_level").findElementById("layer").findElementById("panel").findElementById("status_text").getRenderer(TextRenderer.class);
        if("null".equals(name)) {
            enqueue(() -> {
                worldManager.closeLevel();
                lobby();
                return null;
            });
            return;
        }

        new Thread(() -> {
          try {
              enqueue(() -> {
                  nifty.gotoScreen("load_level");
                  statusText.setText("Loading Terrain..");
                  return null;
              }).get();
              worldManager.loadLevel(name);
              enqueue(() -> {
                  statusText.setText("Creating NavMesh..");
                  return null;
              }).get();
//              worldManager.createNavMesh();
              enqueue(() -> {
                  statusText.setText("Loading Models..");
                  return null;
              }).get();
//              worldManager.preloadModels(modelNames);
              enqueue(() -> {
                  worldManager.attachLevel();
                  statusText.setText("Done Loading!");
                  nifty.gotoScreen("default_hub");
                  inputManager.setCursorVisible(false);
                  return null;
              }).get();
          }catch (Exception e) {
              e.printStackTrace();
          }
        }).start();
    }


    /**
     * sets the status text of the main login view, threadsafe
     * 设置主登录视图的状态文本，线程安全
     * @param text
     */
    public void setStatusText(String text) {
        enqueue(() -> {
            statusText.setText(text);
            return null;
        });
    }

    /**
     * brings up the lobby display
     * 跳转到游戏大厅页面
     */
    public void lobby() {
        inputManager.setCursorVisible(false);
        nifty.gotoScreen("lobby");
    }
}
