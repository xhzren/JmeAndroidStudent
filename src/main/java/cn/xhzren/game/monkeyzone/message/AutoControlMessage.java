package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.control.NetworkedAutonomousControl;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;
import lombok.ToString;

import java.util.logging.Level;
import java.util.logging.Logger;

@Serializable()
@ToString
public class AutoControlMessage extends PhysicsSyncMessage {

    public Vector3f aimAt = Vector3f.ZERO;
    public Vector3f moveTo = Vector3f.ZERO;

    public AutoControlMessage() {}

    public AutoControlMessage(long id, Vector3f aimAt, Vector3f moveTo) {
        this.syncId = id;
        this.aimAt = aimAt;
        this.moveTo = moveTo;
    }

    @Override
    public void appData(Object object) {
        NetworkedAutonomousControl netControl = ((Spatial)object).getControl(NetworkedAutonomousControl.class);
        assert (netControl != null);
        if(netControl == null) {
            Logger.getLogger(AutoControlMessage.class.getName()).log(Level.SEVERE, "Entity {0} has to Autonomous Control, message not accepted", new Object[]{((Spatial) object).getUserData("entity_id")});
            return;
        }
        netControl.aimAt(aimAt);
        netControl.moveTo(moveTo);
    }
}
