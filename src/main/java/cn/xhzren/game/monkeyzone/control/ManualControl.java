package cn.xhzren.game.monkeyzone.control;

import com.jme3.math.Vector3f;


/**
 * Interface for manual movement controls, used by humans locally and via network
 * @author normenhansen
 * 人工运动控制的接口, 供人类在本地和通过网络使用.
 */
public interface ManualControl extends MovementControl  {

    /**
     * X轴转向
     * @param value
     */
    void steerX(float value);

    /**
     * Y轴转向
     * @param value
     */
    void steerY(float value);

    /**
     * 瞄准方向
     * @return
     */
    Vector3f getAimDirection();

    /**
     * 位置
     * @return
     */
    Vector3f getLocation();

    /**
     * X轴移动
     * @param value
     */
    void moveX(float value);
    /**
     * Y轴移动
     * @param value
     */
    void moveY(float value);
    /**
     * Z轴移动
     * @param value
     */
    void moveZ(float value);

    /**
     * 执行动作
     * @param button
     * @param pressed
     */
    void performAction(int button, boolean pressed);
}
