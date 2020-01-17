package cn.xhzren.game.monkeyzone.ai.commands;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.ai.AbstractCommand;
import cn.xhzren.game.monkeyzone.ai.Command;
import cn.xhzren.game.monkeyzone.ai.SphereTrigger;
import cn.xhzren.game.monkeyzone.control.AutonomousControl;
import cn.xhzren.game.monkeyzone.message.ActionMessage;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Simple 攻击命令
 */
public class AttackCommand extends AbstractCommand {

    private float timer = 0f;
    private float attackTime = 5f;

    @Override
    public Command initialize(WorldManager world, long playerId, long entityId, Spatial spat) {
        return super.initialize(world, playerId, entityId, spat);
    }

    @Override
    public TargetResult setTargetEntity(Spatial spat) {
        int groupId = spat.getUserData("group_id");
        if(groupId != -1 && groupId != (int)entity.getUserData("group_id")) {
            return super.setTargetEntity(spat);
        }
        return TargetResult.Deny;
    }

    @Override
    public TargetResult setTargetLocation(Vector3f location) {
        return TargetResult.Deny;
    }

    @Override
    public State doCommand(float tpf) {
        timer+=tpf;
        if(timer >= attackTime) {
            timer = 0;
            //check if still in range
            //检查是否仍在范围内
            if (entity.getControl(SphereTrigger.class).getGhost().
                    getOverlappingObjects().contains(
                            targetEntity.getControl(PhysicsControl.class))) {
                //移动到目标的世界坐标
                entity.getControl(AutonomousControl.class).moveTo(targetEntity.getWorldTranslation());
                //触发射击动作
                entity.getControl(AutonomousControl.class).performAction(ActionMessage.SHOOT_ACTION, true);
            }else {
                //完成一次动作
                return State.Finished;
            }
        }

        //更新HitPoints信息
        Float targetHP = targetEntity.getUserData("HitPoints");
        if(targetHP != null && targetHP < 0) {
            return State.Finished;
        }
        return State.Blocking;
    }

    @Override
    public String getName() {
        return "Attack";
    }
}
