package cn.xhzren.game.monkeyzone.ai;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.control.CommandControl;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Sphere trigger attached to every autonomous entity, contains command that is
 * executed when entity enters and command!=null.
 * @author normenhansen
 * 附加到每个独立实体的球形触发器,
 * 包含在实体进入时执行的命令并且command != null,
 */
public class SphereTrigger implements TriggerControl {

    protected Spatial spatial;
    protected boolean enabled = true;
    protected GhostControl ghostControl;
    protected float checkTimer = 0;
    protected float checkTime = 1;
    protected WorldManager world;
    protected CommandControl queueControl;//命令控制器
    protected Command command;//要执行的命令

    public SphereTrigger(WorldManager world)  {
        this.world = world;
    }

    public SphereTrigger(WorldManager world, Command command)  {
        this.world = world;
        this.command = command;
    }

    @Override
    public void update(float tpf) {
        if(!enabled) {
            return;
        }
        checkTimer += tpf;
        if(checkTimer >= checkTime) {
            checkTimer = 0;
            //如果重叠对象大于0
            if(command != null && ghostControl.getOverlappingCount() > 0) {
                List<PhysicsCollisionObject> objects = ghostControl.getOverlappingObjects();
                for (Iterator<PhysicsCollisionObject> it = objects.iterator(); it.hasNext();) {
                    //循环获取所有重叠对象
                    PhysicsCollisionObject physicsCollisionObject = it.next();
                    Spatial targetEntity = world.getEntity(physicsCollisionObject);
                    //如果对象不为空, 并且不是自己本身
                    if(targetEntity != null && spatial.getUserData("player_id")
                            != targetEntity.getUserData("player_id")) {
                        Command.TargetResult info = command.setTargetEntity(targetEntity);
                        if(info == Command.TargetResult.Accept ||
                                info == Command.TargetResult.AcceptEnemy ||
                                info == Command.TargetResult.AcceptFriendly) {
                            if(!command.isRunning()) {
                                queueControl.addCommand(command);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public void setSpatial(Spatial spatial) {
        if(spatial == null) {
            //如果传来的对象是空
            if(this.spatial != null) {
                //如果本身对象不为空
                //先移除自身的ghostControl
                this.spatial.removeControl(ghostControl);
                //再删除世界中的ghostControl
                world.getPhysicsSpace().remove(ghostControl);
            }
            //把本身对象置空,返回
            this.spatial = spatial;
            return;
        }

        //得到新对象
        this.spatial = spatial;
        if(ghostControl == null) {
            //创建新的ghostControl
            ghostControl = new GhostControl(new SphereCollisionShape(10));
        }
        //设置到自身和世界
        spatial.addControl(ghostControl);
        world.getPhysicsSpace().add(ghostControl);

        queueControl = spatial.getControl(CommandControl.class);
        //如果命令不为空, 初始化commandControl
        if(command != null) {
            queueControl.initializeCommand(command);
        }
        if(queueControl == null) {
            //如果commandControl为空, 抛异常
            throw new IllegalStateException("Cannot add AI control to spatial without CommandQueueControl");
        }
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public GhostControl getGhost() {
        return ghostControl;
    }

    public void setGhostRadius(float radius) {
        ghostControl.setCollisionShape(new SphereCollisionShape(radius));
    }
    public void setCheckTime(float checkTime) {
       this.checkTime = checkTime;
    }
        public void setCommand(Command command) {
        this.command = command;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void render(RenderManager renderManager, ViewPort viewPort) {

    }

    @Override
    public void write(JmeExporter jmeExporter) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void read(JmeImporter jmeImporter) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }
}
