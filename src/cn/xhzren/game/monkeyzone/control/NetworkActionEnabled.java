package cn.xhzren.game.monkeyzone.control;

import com.jme3.scene.control.Control;

/**
 * Interface used to unify performing actions for autonomous and manual controls
 * @author normenhansen
 * 统一用于自主控制和手动控制的执行动作的接口.
 */
public interface NetworkActionEnabled extends Control {

    /**
     * 执行动作
     * @param action
     * @param activate
     */
    void doPerformAction(int action, boolean activate);
}
