package cn.xhzren.test.gui;

import cn.xhzren.avg.BranchDialogHelper;
import cn.xhzren.avg.DialogHelper;
import cn.xhzren.avg.entity.DialogOption;
import cn.xhzren.avg.entity.DialogEnter;
import cn.xhzren.avg.entity.OptionItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.*;
import com.simsilica.lemur.anim.SpatialTweens;
import com.simsilica.lemur.anim.TweenAnimation;
import com.simsilica.lemur.anim.Tweens;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.event.PopupState;
import com.simsilica.lemur.style.ElementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogDemoState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(DialogDemoState.class);

    private Container mainWindow;
    private Label name;
    private Label content;
    //剧情类型: 1主线2支线
    public static int storyType = 1;
    private EventCommon eventCommon = new EventCommon();

    @Override
    protected void initialize(Application app) {
        mainWindow = new Container();
        mainWindow.setLocalTranslation(getApplication().getCamera().getWidth() * 0.1f, 100, 0);

        name = mainWindow.addChild(new Label(DialogHelper.currentDialog.getName(),new ElementId("window.title.label")));
        name.setFontSize(24);
        content = mainWindow.addChild(
                new Label(DialogHelper.currentDialog.getContent().get(DialogHelper.currentDialog.getCurrentIndex())));
        content.setTextHAlignment(HAlignment.Left);
        content.setMaxWidth(400);
        content.setPreferredSize(new Vector3f(getApplication().getCamera().getWidth() * 0.8f,100, 0));

        MouseEventControl.addListenersToSpatial(mainWindow, new DefaultMouseListener() {
            @Override
            protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
                log.info("click label size" + ((Container)target).getSize());
                setupDialog();
            }
        });
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
        //区分选项和文本的对话类型
        if("text".equals(jsonObject.getString("type"))) {
            DialogEnter dialogEnter = JSONObject.parseObject(jsonObject.toJSONString(), DialogEnter.class);
            content.setText(dialogEnter.getContent().get(dialogEnter.getCurrentIndex()));
            name.setText(dialogEnter.getName());
        }else if("option".equals(jsonObject.getString("type"))) {
            DialogOption option = JSONObject.parseObject(jsonObject.toJSONString(), DialogOption.class);
            getState(PopupState.class).showPopup(makeOption(option));
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
                    eventCommon.getClass().getMethod(item.getEvent(), new Class[]{}).invoke(
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
        GuiGlobals.getInstance().requestFocus(mainWindow);
    }

    @Override
    protected void onDisable() {
        mainWindow.removeFromParent();
    }
}
