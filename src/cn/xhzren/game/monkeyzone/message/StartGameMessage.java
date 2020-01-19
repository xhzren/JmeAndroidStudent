package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Message;
import com.jme3.network.serializing.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * used to load a level on the client and to report that a level has been loaded
 * to the server.
 * 用于在客户端上加载日志级别, 并报告已将级别加载到服务器.
 * @author normenhansen
 */
@Serializable()
@ToString
public class StartGameMessage extends AbstractMessage {

    public String levelName;
    public String[] modelNames;

    public StartGameMessage() {
    }

    public StartGameMessage(String levelName) {
        this.levelName = levelName;
    }

    public StartGameMessage(String levelName, String[] modelNames) {
        this.levelName = levelName;
        this.modelNames = modelNames;
    }
}
