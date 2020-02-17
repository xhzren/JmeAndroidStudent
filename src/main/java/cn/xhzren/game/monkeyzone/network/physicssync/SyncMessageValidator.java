package cn.xhzren.game.monkeyzone.network.physicssync;

/**
 * 同步消息验证器
 */
public interface SyncMessageValidator {

    public boolean checkMessage(PhysicsSyncMessage message);
}
