package cn.xhzren.netty.appstates;

import cn.xhzren.netty.entity.TaskEntity;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ItemQueueAppState extends BaseAppState {

    static Logger logger = LoggerFactory.getLogger(ItemQueueAppState.class);

    private SimpleApplication application;
    private Node root;

    private List<TaskEntity> taskEntities;

    @Override
    protected void initialize(Application app) {
        application = (SimpleApplication)app;
        root = new Node("itemQueue");
        taskEntities = new ArrayList<>();
    }



    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public void addTaskEntities(TaskEntity task) {
        taskEntities.add(task);
    }

    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }
}
