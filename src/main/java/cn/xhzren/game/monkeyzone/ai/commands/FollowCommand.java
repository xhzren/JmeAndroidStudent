package cn.xhzren.game.monkeyzone.ai.commands;

import cn.xhzren.game.monkeyzone.ai.AbstractCommand;
import cn.xhzren.game.monkeyzone.control.AutonomousControl;

/**
 * Simple 跟随命令
 */
public class FollowCommand extends AbstractCommand {

    float timer = 0;
    float updateTime = 0.25f;

    @Override
    public State doCommand(float tpf) {
        timer += tpf;
        if(timer > updateTime) {
            if(targetEntity != null) {
                targetLocation.set(targetEntity.getWorldTranslation());
            }
            entity.getControl(AutonomousControl.class).moveTo(targetLocation);
            timer = 0;
        }
        return State.Blocking;
    }

    @Override
    public String getName() {
        return "Follow";
    }
}
