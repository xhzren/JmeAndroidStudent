package cn.xhzren.netty.appstates;

import cn.xhzren.netty.entity.TaskRangeEntity;
import cn.xhzren.netty.util.Constancts;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MapAppState extends BaseAppState {

    static Logger logger = LoggerFactory.getLogger(MapAppState.class);

    private SimpleApplication application;
    private Node root;
    private Node posTag;
    private Material guiMat;
    private List<String> names;

    @Override
    protected void initialize(Application app) {
        application = (SimpleApplication)app;
        root = new Node("sortMap");
        posTag = new Node("posTag");
        guiMat = new Material(app.getAssetManager(),Constancts.GUI_MATERIALS);
    }

    public void makeTag(String childName) {
        application.getStateManager().getState(ItemQueueAppState.class)
                .getTaskEntities().stream().filter(e->e.isCurrentTag()).
                forEach((e)-> {
                    e.getRangeEntities().forEach((range)-> {
                        logger.info("range : {}", range);
                    });
                });

        Node n = (Node)root.getChild(childName);
        Node child = new Node(childName);
        posTag.attachChild(child);
        n.getChildren().forEach((e) -> {
            guiMat.setColor("Color", ColorRGBA.randomColor());
            child.attachChild(makePosSpatial(e.getLocalTranslation()));
        });
    }

    private Geometry makeIrregularRange(Vector2f start,Vector2f end) {
        Line line = new Line(new Vector3f(start.x,start.y,0), new Vector3f(end.x,end.y,0));
        Geometry rand = new Geometry("",line);
        guiMat.setColor("Color", ColorRGBA.randomColor());
        rand.setMaterial(guiMat);
        return rand;
    }

    private Geometry makePosSpatial(Vector3f pos) {
        Line line = new Line(pos, pos.add(1, 0, 0));
        Geometry rand = new Geometry("",line);
        rand.setMaterial(guiMat);
        return rand;
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
}
