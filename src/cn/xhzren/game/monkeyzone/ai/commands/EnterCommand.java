package cn.xhzren.game.monkeyzone.ai.commands;

import cn.xhzren.game.monkeyzone.ai.AbstractCommand;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class EnterCommand extends AbstractCommand {

    float timer = 0;

    @Override
    public TargetResult setTargetLocation(Vector3f location) {
        return TargetResult.Deny;
    }

    @Override
    public TargetResult setTargetEntity(Spatial spat) {
        Long playerId = spat.getUserData("player_id");
        if(playerId == -11) {
            targetLocation = spat.getWorldTranslation();
        }
        return TargetResult.Deny;
    }

    @Override
    public State doCommand(float tpf) {
        return null;
    }

    @Override
    public String getName() {
        return "Enter";
    }
}
