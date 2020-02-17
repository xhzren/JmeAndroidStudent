package cn.xhzren.game.monkeyzone.ai;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.control.CommandControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public abstract class AbstractCommand implements Command {

    protected int priority = 1;
    protected long playerId = 1;
    protected long entityId = -1;
    protected Spatial entity = null;
    protected long targetPlayerId = -1;
    protected long targetEntityId = -1;
    protected Spatial targetEntity = null;
    protected Vector3f targetLocation = new Vector3f();
    private boolean running = false;
    protected WorldManager world;

    public abstract State doCommand(float tpf);

    /**
     * 初始化
     * @param entityId 实体id
     * @param playerId 玩家id
     * @param entity 实体
     * @param world 世界
     * @return
     */
    @Override
    public Command initialize(WorldManager world, long playerId, long entityId, Spatial spat) {
        this.entityId = entityId;
        this.playerId = playerId;
        this.entity = spat;
        this.world = world;
        return this;
    }

    @Override
    public TargetResult setTargetEntity(Spatial spat) {
        this.targetPlayerId = spat.getUserData("player_id");
        this.targetEntityId = spat.getUserData("entity_id");
        this.targetEntity = spat;
        targetLocation.set(spat.getLocalTranslation());
        return TargetResult.Accept;
    }

    @Override
    public TargetResult setTargetLocation(Vector3f location) {
        targetLocation.set(location);
        return TargetResult.Accept;
    }

    public void cancel() {
        entity.getControl(CommandControl.class).removeCommand(this);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }
}
