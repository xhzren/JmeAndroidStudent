package cn.xhzren.game.monkeyzone.ai.commands;

import cn.xhzren.game.monkeyzone.ai.AbstractCommand;
import cn.xhzren.game.monkeyzone.control.AutonomousControl;
/**
 * Simple move to location command
 * @author normenhansen
 * 移动到位置的简单命令
 */

public class MoveCommand extends AbstractCommand {

    @Override
    public State doCommand(float tpf) {
        entity.getControl(AutonomousControl.class).moveTo(targetLocation);
        if(!entity.getControl(AutonomousControl.class).isMoving()) {
            return State.Finished;
        }
        return State.Continuing;
    }

    @Override
    public String getName() {
        return "Move";
    }
}
