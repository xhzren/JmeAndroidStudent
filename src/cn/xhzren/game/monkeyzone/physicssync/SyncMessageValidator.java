package cn.xhzren.game.monkeyzone.physicssync;

/**
 * 同步消息验证器
 */
public interface SyncMessageValidator {

    public boolean checkMessage(PhysicsSyncMessage message);
}
