package cn.xhzren.game.monkeyzone.control;

import com.jme3.math.Vector3f;
import com.jme3.scene.control.Control;

/**
 * Base interface for autonomous and manual movements controls, mostly used
 * to read position of entity
 * @author normenhansen
 * 用于自主和手动运动控件的基本接口, 主要用于读取实体的位置.
 */
public interface MovementControl extends Control {

    Vector3f getLocation();
    Vector3f getAimDirection();
}
