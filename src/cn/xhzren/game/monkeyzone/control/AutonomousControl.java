package cn.xhzren.game.monkeyzone.control;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;

/**
 * Basic interface for autonomous movement controls, these are used by AI to move entities.
 * When a NavigationControl is available on the spatial, it should be used by
 * the AutonomousControl to navigate.
 * @author normenhansen
 * 自主运动控制的基本接口, AI使用这些接口来移动实体.
 * 当空间上有NavigationControl时, AutonomousControl应该使用它进行导航.
 */
public interface AutonomousControl extends MovementControl {

    /**
     *aim at location, return false if not possible (max view range, obstacles)
     * 看向某个位置, 不会返回false(最大视野范围, 障碍物)
     * @param direction
     */
    void aimAt(Vector3f direction);

    /**
     * 执行动作, 与玩家按下按钮相同
     * @param action
     */
    void performAction(int action, boolean activate);

    /**
     * move to location by means of this control, should use NavigationControl if available
     * 通过此控件移至位置. 如果NavigationControl可用, 应使用它.
     * @param location
     * @return false if already at location, uses radius from NavigationControl if it exists.
     * @return 如果已经存在, 则返回false; 如果存在, 则使用NavigationControl的半径.
     */
    void moveTo(Vector3f location);

    /**
     * checks if this entity is moving
     * 检查此实体是否在移动
     * @return
     */
    boolean isMoving();

    /**
     * gets the current target location of this entity.
     * 获得此实体的当前目标位置
     * @return
     */
    Vector3f getTargetLocation();

    /**
     * gets the current location of this entity.
     * 获得此实体的当前位置
     * @return
     */
    Vector3f getLocation();

    /**
     * gets the aim direction of this entity.
     * 获得此实体的目标方向
     * @return
     */
    Vector3f getAimDirection();
}
