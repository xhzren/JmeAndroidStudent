package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.control.NetworkActionEnabled;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;
import lombok.ToString;

/**
 * perform action for player (human and AI), used bidirectional
 * @author normenhansen
 * 为玩家和ai执行动作, 双向使用.
 */
@Serializable
@ToString
public class ActionMessage extends PhysicsSyncMessage {
    public static int NULL_ACTION = 0;
    public static int JUMP_ACTION = 1;
    public static int ENTER_ACTION = 2;
    public static int SHOOT_ACTION = 3;

    public int action;
    public boolean pressed;

    public ActionMessage() {
    }

    public ActionMessage(long id, int action, boolean pressed) {
        super(id);
        this.action = action;
        this.pressed = pressed;
    }

    @Override
    public void appData(Object object) {
        ((Spatial)object).getControl(NetworkActionEnabled.class).doPerformAction(action, pressed);
    }
}
