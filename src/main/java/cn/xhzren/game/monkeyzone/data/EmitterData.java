package cn.xhzren.game.monkeyzone.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class EmitterData {
    public Node emit;
    public long id;
    public String effectName;
    public Vector3f location;
    public Vector3f endLocation;
    public Quaternion rotation;
    public Quaternion endRotation;
    public float timer;
    public float curTime = 0;

    public EmitterData(Node emit, long id, String effectName, Vector3f location, Vector3f endLocation, Quaternion rotation, Quaternion endRotation, float timer) {
        this.emit = emit;
        this.id = id;
        this.effectName = effectName;
        this.location = location;
        this.endLocation = endLocation;
        this.rotation = rotation;
        this.endRotation = endRotation;
        this.timer = timer;
    }
}
