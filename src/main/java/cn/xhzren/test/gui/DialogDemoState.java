package cn.xhzren.test.gui;

import cn.xhzren.avg.*;
import cn.xhzren.avg.common.EventCommon;
import cn.xhzren.avg.entity.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.event.PopupState;
import com.simsilica.lemur.style.ElementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogDemoState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(DialogDemoState.class);

    private AssetManager assetManager;

    private Container mainWindow;
    private Container toolbar;
    private Label name;
    private Label content;
    //剧情类型: 1主线2支线
    public static int storyType = 1;
    private EventCommon eventCommon = new EventCommon();

    @Override
    protected void initialize(Application app) {
        assetManager = app.getAssetManager();

        mainWindow = new Container();
        mainWindow.setLocalTranslation(0, Constant.HEIGHT * 0.27f, 0);

        Button close = mainWindow.addChild(new Button(""));
        IconComponent closeIcon= new IconComponent("Textures/Avg/close.png");
        closeIcon.setIconSize(new Vector2f(20, 20));
        close.setIcon(closeIcon);

        name = mainWindow.addChild(new Label(DialogHelper.currentDialog.getName(),new ElementId("window.title.label")));
        name.setFontSize(24);
        content = mainWindow.addChild(
                new Label(DialogHelper.currentDialog.getContent().get(DialogHelper.currentDialog.getCurrentIndex())));
        content.setTextHAlignment(HAlignment.Left);
        content.setMaxWidth(Constant.WIDTH * 0.8f);
        content.setPreferredSize(new Vector3f(Constant.WIDTH * 0.8f,Constant.HEIGHT * 0.27f, 0));

        toolbar = new Container();
        toolbar.setLocalTranslation(Constant.WIDTH * 0.9f, Constant.HEIGHT * 0.27f, 0);
        Container buttons = toolbar.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y)));
        buttons.setLocalScale(1.3f);
        buttons.setBackground(new QuadBackgroundComponent(ColorRGBA.randomColor()));
        buttons.addChild(new Button("auto"), 0).addClickCommands((e)-> {
            log.info("自动");
        });
        buttons.addChild(new Button("skip"), 0).addClickCommands((e)-> {
            log.info("跳过");
        });
        buttons.addChild(new Button("save"),1).addClickCommands((e)-> {
            log.info("保存");
            save(null,1);
        });
        buttons.addChild(new Button("read"),2).addClickCommands((e)-> {
            log.info("读取");
            setEnabled(false);
            getState(ArchiveRecordDemoState.class).setEnabled(true);
        });
        buttons.addChild(new Button("exit"),3).addClickCommands((e) -> {
            log.info("退出");
        });

        MouseEventControl.addListenersToSpatial(mainWindow, new DefaultMouseListener() {
            @Override
            protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
                log.info("click label size" + ((Container)target).getSize());
                setupDialog();
            }
        });

        setEnabled(false);
    }

    public void save(ArchiveRecords records,int saveType) {
        ArchiveRecords save = new ArchiveRecords();
        if(records != null) {
            save = records;
        }
        save.setRecordName(DialogHelper.chapterContent.getString("name"));
            save.setCurrentDialog(DialogHelper.currentDialog);
            save.setCurrentDialog(BranchDialogHelper.currentBranchDialog);
        save.setTime(DateUtils.nowDateToString(DateUtils.TranRule.YMDHMS));
        getState(ScreenshotAppState.class).takeScreenshot();
        getState(ScreenshotAppState.class).postFrame(null);
        save.setImage(FileHelper.getArchiveCover());
        FileHelper.writeRecords(save, saveType);
    }

    private void setupDialog() {
        JSONObject jsonObject = null;
        if(storyType == 1) {
            jsonObject = DialogHelper.nextDialog();
        }else if(storyType == 2) {
            jsonObject = BranchDialogHelper.nextDialog();
        }
        if(jsonObject == null) {
            content.setText("结束!");
            content.setTextHAlignment(HAlignment.Center);
            name.setText("");
            return;
        }

        if(jsonObject.getJSONObject("event") != null) {
            EventEnter event = JSONObject.parseObject(jsonObject.getJSONObject("event").toJSONString(), EventEnter.class);
            try {
            if("changeBG".equals(event.getName())) {
                getState(BackgroundDemoState.class).changeBG((String)event.getParams().get(0));
            }
            }catch (Exception e) {

            }
        }

        //区分选项和文本的对话类型
        if("text".equals(jsonObject.getString("type"))) {
            DialogEnter dialogEnter = JSONObject.parseObject(jsonObject.toJSONString(), DialogEnter.class);
            content.setText(dialogEnter.getContent().get(dialogEnter.getCurrentIndex()));
            name.setText(dialogEnter.getName());
        }else if("option".equals(jsonObject.getString("type"))) {
            DialogOption option = JSONObject.parseObject(jsonObject.toJSONString(), DialogOption.class);
            getState(PopupState.class).showModalPopup(makeOption(option));
        }else if("ending".equals(jsonObject.getString("type"))) {
//            EndingEnter ending = JSONObject.parseObject(jsonObject.toJSONString(), EndingEnter.class);
            EndingEnter ending = DialogHelper.getEndingById(jsonObject.getString("jump"));
            if(ending == null) {
                ending = DialogHelper.defaultEnding;
            }
            getState(PopupState.class).showModalPopup(makeEnding(ending));
        }
    }

    /**
     * 制作选项弹窗
     * @param option 选项数据
     * @return 弹窗对象
     */
    private Container makeOption(DialogOption option) {
        stealth();
        Container optionPlane = new Container();
        optionPlane.setLocalTranslation(getApplication().getCamera().getWidth() * 0.5f,
                getApplication().getCamera().getHeight() * 0.5f, 0);
        optionPlane.addChild(new Label(option.getName(),new ElementId("window.title.label")));

        for(OptionItem item: option.getOptionItems()) {
            Button tmp = optionPlane.addChild(new Button(item.getContent()));
            //绑定各个选项对应的事件
            tmp.addClickCommands((e)-> {
                try {
                    eventCommon.getClass().getMethod(item.getEvent().getName(), new Class[]{}).invoke(
                            eventCommon, new Object[]{});
                }catch (Exception ex){
                    log.error(ex.getMessage());
                    DialogHelper.backDialog();
                    return;
                }
                optionPlane.removeFromParent();
                item.getDialogList();
                if(item.getDialogList() != null && item.getDialogList().size()>0) {
                    BranchDialogHelper.branchDialogList = JSONArray.parseArray(JSON.toJSONString(item.getDialogList()));
                    BranchDialogHelper.jumpId = option.getJump();
                    BranchDialogHelper.jumpIndex = item.getJumpIndex();
                    storyType = 2;
                }
                setupDialog();
                show();
            });
        }
        return optionPlane;
    }


    /**
     * 制作Ending弹窗
     * @param ending Ending数据
     * @return 弹窗对象
     */
    private Container makeEnding(EndingEnter ending) {
        stealth();
        Container endingPlane = new Container();
        endingPlane.setPreferredSize(new Vector3f(getApplication().getCamera().getWidth(),
                getApplication().getCamera().getHeight(),0));
        endingPlane.setLocalTranslation(0,
                getApplication().getCamera().getHeight(), 0);
        endingPlane.addChild(new Label(ending.getName(),new ElementId("window.title.label")));

        endingPlane.addChild(new Panel(endingPlane.getSize().getX() * 0.2f,
                endingPlane.getSize().getY() * 0.3f)).setBackground(
                new QuadBackgroundComponent(assetManager.loadTexture(ending.getImage()))
        );
        endingPlane.addMouseListener(new DefaultMouseListener(){
            @Override
            protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
                log.info("结束！返回标题！");
                getStateManager().attach(new TitleDemoState());
                setEnabled(false);
                endingPlane.removeFromParent();
                show();
            }
        });
        return endingPlane;
    }


    /**
     * 隐藏对话框
     */
    private void stealth() {
        mainWindow.setAlpha(0f);
    }
    private void show() {
        mainWindow.setAlpha(1f);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        Node gui = ((TestLemur)getApplication()).getGuiNode();
        gui.attachChild(mainWindow);
        gui.attachChild(toolbar);
        GuiGlobals.getInstance().requestFocus(mainWindow);
    }

    @Override
    protected void onDisable() {
        mainWindow.removeFromParent();
        toolbar.removeFromParent();
    }
}
