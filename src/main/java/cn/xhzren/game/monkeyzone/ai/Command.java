package cn.xhzren.game.monkeyzone.ai;

import cn.xhzren.game.monkeyzone.WorldManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Basic interface for AI commands
 * @author normenhansen
 * ai命令的基本接口.
 */
public interface Command {
    enum State {
        /**
         * 已完成
         */
        Finished,

        /**
         * 持续
         */
        Continuing,

        /**
         * 堵塞
          */
        Blocking;
    }

    enum TargetResult {
        /**
         * 拒绝
         */
        Deny,

        /**
         * 接受
         */
        Accept,

        /**
         * 拒绝朋友
         */
        DenyFriendly,

        /**
         * 拒绝敌人
         */
        DenyEnemy,

        /**
         * 接受敌人
         */
        AcceptEnemy,

        /**
         * 接受朋友
         */
        AcceptFriendly;
    }

    String getName();

    /**
     * 执行命令
     * @param tpf
     * @return
     */
    State doCommand(float tpf);

    /**
     * 初始化
     * @param world
     * @param playerId
     * @param entityId
     * @param spat
     * @return
     */
    Command initialize(WorldManager world, long playerId, long entityId, Spatial spat);


    TargetResult setTargetEntity(Spatial spat);

    TargetResult setTargetLocation(Vector3f location);

    /**
     * 获得优先级
     * @return
     */
    int getPriority();

    /**
     * 设置优先级
     * @param priority
     */
    void setPriority(int priority);

    /**
     * 是否在运行
     * @return
     */
    boolean isRunning();

    /**
     * 设置运行状态
     * @param running
     */
    void setRunning(boolean running);

    void cancel();
}
