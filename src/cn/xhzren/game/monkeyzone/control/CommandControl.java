package cn.xhzren.game.monkeyzone.control;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.ai.Command;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Handles the command queue of an AI entity.
 * @author normenhansen
 * 处理AI实体的命令队列.
 */
public class CommandControl implements Control {

    protected Spatial spatial;
    protected boolean enabled = true;//是否启用
    //命令队列
    protected LinkedList<Command> commands = new LinkedList<>();

    protected Command defaultCommand;
    //玩家id
    protected long playerId;
    protected long entityId;
    protected WorldManager world;

    public CommandControl(long playerId, long entityId, WorldManager world) {
        this.playerId = playerId;
        this.entityId = entityId;
        this.world = world;
    }

    public Command initializeCommand(Command command) {
        return command.initialize(world, entityId, playerId, spatial);
    }

    public void addCommand(Command command) {
        command.setRunning(true);
        for (int i=0;i<commands.size();i++) {
            Command tempCommand = commands.get(i);
            if(tempCommand.getPriority() < command.getPriority()) {
                commands.add(i, command);
                return;
            }
        }
        commands.add(command);
    }

    public void removeCommand(Command command) {
        command.setRunning(false);
        commands.remove(command);
    }

    public void clearCommand() {
        for (Iterator<Command> it = commands.iterator(); it.hasNext();) {
            Command command = it.next();
            command.setRunning(true);
        }
        commands.clear();
    }

    @Override
    public void update(float tpf) {
        if(!enabled) {
            return;
        }
        for (Iterator<Command> it = commands.iterator();it.hasNext();) {
            Command command = it.next();
            //do command and remove if returned true, else stop processing
            //如果返回true, 执行命令并删除, 否则停止处理
            Command.State commandState = command.doCommand(tpf);
            switch (commandState) {
                case Finished:
                    command.setRunning(false);
                    it.remove();
                    break;
                case Blocking:
                    return;
                case Continuing:
                    return;
            }
        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        if(spatial == null) {
            if(this.spatial != null) {

            }
            this.spatial = spatial;
            return;
        }
        this.spatial = spatial;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
