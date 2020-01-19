package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;
import lombok.ToString;

/**
 * Sync message for character objects
 * 角色对象的同步消息.
 * @author normenhansen
 */
@Serializable()
@ToString
public class SyncCharacterMessage extends PhysicsSyncMessage {

    public Vector3f location = new Vector3f();
    public Vector3f walkDirection = new Vector3f();
    public Vector3f viewDirection = new Vector3f();

    public SyncCharacterMessage() {
    }

    public SyncCharacterMessage(long id, CharacterControl character) {
       this.syncId = id;
       character.getPhysicsLocation(location);
       this.walkDirection.set(character.getWalkDirection());
       this.viewDirection.set(character.getViewDirection());
    }

    public void readData(CharacterControl character) {
        character.getPhysicsLocation(location);
        this.walkDirection.set(character.getWalkDirection());
        this.viewDirection.set(character.getViewDirection());
    }

    @Override
    public void appData(Object character) {
        ((Spatial)character).getControl(CharacterControl.class).setPhysicsLocation(location);
        ((Spatial)character).getControl(CharacterControl.class).setWalkDirection(walkDirection);
        ((Spatial)character).getControl(CharacterControl.class).setViewDirection(location);
    }
}
