package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.ClientEffectsManager;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * Message sent to play effect on client
 * 发送消息作用于客户端
 * @author normenhansen
 */
@Serializable()
public class ServerEffectMessage extends PhysicsSyncMessage {

    public long effectId;
    public String name;
    public Vector3f location;
    public Quaternion rotation;
    public Vector3f endLocation;
    public Quaternion endRotation;
    public float playTime;

    public ServerEffectMessage() {
    }

    public ServerEffectMessage(long id, String name, Vector3f location, Quaternion rotation, Vector3f endLocation, Quaternion endRotation, float playTime) {
        syncId = -2;
        this.effectId = id;
        this.name = name;
        this.location = location;
        this.rotation = rotation;
        this.endLocation = endLocation;
        this.endRotation = endRotation;
        this.playTime = playTime;
    }

    @Override
    public void appData(Object object) {
        ClientEffectsManager manager = (ClientEffectsManager)object;
        manager.playEffect(effectId, name,location, endLocation, rotation,endRotation, playTime);
    }
}
