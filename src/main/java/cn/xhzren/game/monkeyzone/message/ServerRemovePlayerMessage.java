package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.WorldManager;
import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;

public class ServerRemovePlayerMessage extends PhysicsSyncMessage {

    public long playerId;

    public ServerRemovePlayerMessage() {
    }

    public ServerRemovePlayerMessage(long playerId) {
        syncId = -1;
        this.playerId = playerId;
    }

    @Override
    public void appData(Object object) {
        WorldManager manager = (WorldManager)object;
        manager.removePlayer(playerId);
    }
}
