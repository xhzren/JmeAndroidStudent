package cn.xhzren.game.monkeyzone.control;

import cn.xhzren.game.monkeyzone.PhysicsSyncManager;
import cn.xhzren.game.monkeyzone.message.ActionMessage;
import cn.xhzren.game.monkeyzone.message.ManualControlMessage;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.network.Client;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;

/**
 * Abstract Manual Control, handles sending to server when client is set
 * Abstract的手动控制器，本身拥有Client对象时处理发送到服务器
 * @author normenhansen
 */
public abstract class NetworkedManualControl implements ManualControl,NetworkActionEnabled {

    protected boolean enabled = true;
    private Client client;
    private long entity_id;
    private float lastSteerX;
    private float lastSteerY;
    private float lastMoveX;
    private float lastMoveY;
    private float lastMoveZ;

    public NetworkedManualControl() {
    }

    public NetworkedManualControl(Client client, long entity_id) {
        this.client = client;
        this.entity_id = entity_id;
    }

    public NetworkedManualControl(PhysicsSyncManager manager, long entity_id) {
        this.entity_id = entity_id;
    }

    @Override
    public void steerX(float value) {
        if (client != null && value != lastSteerX) {
            lastSteerX = value;
            sendMoveSync();
        }
    }

    @Override
    public void steerY(float value) {
        if (client != null && value != lastSteerY) {
            lastSteerY = value;
            sendMoveSync();
        }
    }

    @Override
    public void moveX(float value) {
        if (client != null && value != lastMoveX) {
            lastMoveX = value;
            sendMoveSync();
        }
    }

    @Override
    public void moveY(float value) {
        if (client != null && value != lastMoveY) {
            lastMoveY = value;
            sendMoveSync();
        }
    }

    @Override
    public void moveZ(float value) {
        if (client != null && value != lastMoveZ) {
            lastMoveZ = value;
            sendMoveSync();
        }
    }

    @Override
    public void performAction(int button, boolean pressed) {
        if (client != null) {
            client.send(new ActionMessage(entity_id, button, pressed));
        }
    }

    public abstract void doSteerX(float amount);
    public abstract void doSteerY(float amount);
    public abstract void doMoveX(float amount);
    public abstract void doMoveY(float amount);
    public abstract void doMoveZ(float amount);

    private void sendMoveSync() {
        client.send(new ManualControlMessage(entity_id, lastSteerX, lastSteerY, lastMoveX, lastMoveY, lastMoveZ));
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public abstract void doPerformAction(int action, boolean activate);

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void write(JmeExporter jmeExporter) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void read(JmeImporter jmeImporter) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }
}
