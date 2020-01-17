package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * used by the server to add an entity on the client
 * 服务器用来在客户端上添加实体
 * @author normenhansen
 */
@Serializable()
public class ServerAddEntityMessage extends PhysicsSyncMessage {

    public long entity_id;
    public String modelIdentifier;//模型标识符
    public Vector3f location;
    public Quaternion rotation;

    public ServerAddEntityMessage() {
    }

    public ServerAddEntityMessage(long entity_id, String modelIdentifier, Vector3f location, Quaternion rotation) {
        this.syncId = -1;
        this.entity_id = entity_id;
        this.modelIdentifier = modelIdentifier;
        this.location = location;
        this.rotation = rotation;
    }

    @Override
    public void appData(Object object) {
        WorldManager worldManager = (WorldManager)object;
//        worldManager.add
    }
}
