package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import jogamp.graph.font.typecast.ot.table.ID;
import lombok.ToString;

/**
 * used by the server to add an entity on the client
 *
 * @author normenhansen
 */
@Serializable()
@ToString
public class ServerEnableEntityMessage extends PhysicsSyncMessage {

    public long entityId;
    public Vector3f location;
    public Quaternion rotation;

    public ServerEnableEntityMessage() {
    }

    public ServerEnableEntityMessage(long id, Vector3f location, Quaternion rotation) {
        this.syncId = -1;
        this.entityId = id;
        this.location = location;
        this.rotation = rotation;
    }

    @Override
    public void appData(Object object) {
        WorldManager worldManager = (WorldManager)object;
        worldManager.enableEntity(entityId, location, rotation);
    }
}
