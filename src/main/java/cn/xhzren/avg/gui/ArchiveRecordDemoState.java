package cn.xhzren.avg.gui;

import cn.xhzren.avg.Constant;
import cn.xhzren.avg.DialogHelper;
import cn.xhzren.avg.FileHelper;
import cn.xhzren.avg.entity.ArchiveRecords;
import com.alibaba.fastjson.JSONArray;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.style.ElementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ArchiveRecordDemoState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(ArchiveRecordDemoState.class);

    private AssetManager assetManager;

    private Container mainWindow;
    private Container grids;

    private int startGame = 1;

    public static List<ArchiveRecords> records = new ArrayList<>();

    @Override
    protected void initialize(Application app) {
        updateRecordList();
        assetManager = app.getAssetManager();

        mainWindow = new Container();

        mainWindow.addChild(new Label("Archive Record",new ElementId("window.title.label")));
        mainWindow.setPreferredSize(new Vector3f(Constant.WIDTH, Constant.HEIGHT, 0));
        mainWindow.setLocalTranslation(0, Constant.HEIGHT,0);

        grids = mainWindow.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y)));
        grids.setPreferredSize(new Vector3f(Constant.WIDTH * 0.8f, Constant.HEIGHT * 0.9f, 0));
        //存档子项
        makeRecordItem(0);
        //分页按钮
        Container buttons = grids.addChild(new Container(new SpringGridLayout(Axis.Y,Axis.X, FillMode.None,FillMode.None)), 4);
        for (int i=0;i<Constant.recordCount/4;i++) {
            Button tmp = buttons.addChild(new Button(String.valueOf(i+1)), 0, i);
            tmp.setInsets(new Insets3f(10, 10,0,0));
            tmp.setLocalScale(1.5f);
            tmp.setUserData("index", i);
            tmp.addClickCommands((e)-> {
                makeRecordItem(e.getUserData("index"));
            });
        }
        setEnabled(false);
}

    private void makeRecordItem(int recordPage) {
        grids.getChildren().stream().forEach(e -> {
            if (e.getName() == null) {
                return;
            }
            if (e.getName().contains("recordItem")) {
                e.removeFromParent();
            }
        });
        int countIndex = 0;
        for (int i = 0; i < 4; i++) {
            countIndex = (recordPage * 4) + i;
            Container recordItem = grids.addChild(new Container(new SpringGridLayout(Axis.X, Axis.Y)), i);
            recordItem.setName("recordItem_" + countIndex);
            recordItem.setUserData("countIndex", countIndex);
            recordItem.addChild(new Label("item" + (countIndex), new ElementId("window.title.label")));
            if (getRecordByIndex(countIndex) != null) {
                recordItem.addChild(new Label(getRecordByIndex(countIndex).getRecordName()), 1);
                recordItem.addChild(new Label(getRecordByIndex(countIndex).getTime()), 2);
                MouseEventControl.addListenersToSpatial(recordItem,
                        new DefaultMouseListener() {
                            @Override
                            protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
                                ArchiveRecords record = getRecordByIndex(recordItem.getUserData("countIndex"));
                                getState(PropDemoState.class).setSureCommand((e) -> {
                                    log.info("是否读取当前记录");
                                    DialogHelper.init(record.getCurrentDialog());
                                    Constant.GAME_STATUS = 2;
                                    setEnabled(false);
                                    getState(PropDemoState.class).setEnabled(false);
                                    getState(DialogDemoState.class).setEnabled(true);
                                }).setTitle("is Read?").setEnabled(true);
                            }
                        });
                } else {
                MouseEventControl.addListenersToSpatial(recordItem,
                        new DefaultMouseListener() {
                            @Override
                            protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
                                if (Constant.GAME_STATUS == 2) {
                                    log.info("开始了");
                                    ArchiveRecords records = new ArchiveRecords();
                                    records.setRecordIndex(recordItem.getUserData("countIndex"));
                                    getState(DialogDemoState.class).save(records,2);
                                    updateRecordList();
                                    makeRecordItem((int)recordItem.getUserData("countIndex") / 4);
                                }
                            }
                        });
            }
            recordItem.setInsets(new Insets3f(10, 10, 10, 10));
        }
    }

    public void updateRecordList() {
        records = JSONArray.parseArray(FileHelper.readJsonData(Constant.recordPath), ArchiveRecords.class);
    }

    private ArchiveRecords getRecordByIndex(int index) {
        return records.stream().filter((e)-> {
            return index == e.getRecordIndex();
        }).findFirst().orElse(null);
    }

    public void isCover() {
        log.info("是否覆盖");
    }

    public void isDelete() {
        log.info("是否删除");
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        updateRecordList();
        Node gui = ((TestLemur) getApplication()).getGuiNode();
        gui.attachChild(mainWindow);
        GuiGlobals.getInstance().requestFocus(mainWindow);
    }

    @Override
    protected void onDisable() {
        mainWindow.removeFromParent();
    }

}
