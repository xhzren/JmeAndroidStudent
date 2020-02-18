package cn.xhzren.game.monkeyzone.control;

import cn.xhzren.game.monkeyzone.message.ActionMessage;
import cn.xhzren.game.monkeyzone.message.AutoControlMessage;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;

/**
 * Abstract Autonomous Control, handles sending to server when client is set
 * Abstract自主控制器，在设置客户端时处理向服务器的发送
 * @author normenhansen
 */
public abstract class NetworkedAutonomousControl implements AutonomousControl, NetworkActionEnabled {

    private Client client;
    private long entity_id;
    private Vector3f lastMoveToLocation = new Vector3f();
    private Vector3f lastAimDirection = new Vector3f();
    protected boolean enabled = true;
    protected Spatial spatial;

    public NetworkedAutonomousControl() {

    }

    public NetworkedAutonomousControl(Client client, long entity_id) {
        this.client = client;
        this.entity_id = entity_id;
    }

    @Override
    public void aimAt(Vector3f direction) {
        if(client != null) {
            if(!lastAimDirection.equals(direction)) {
                lastAimDirection.set(direction);
                sendMoveSync();
            }
        }
    }

    @Override
    public void performAction(int action, boolean activate) {
        if(client != null) {
            client.send(new ActionMessage(entity_id, action, activate));
        }
    }

    @Override
    public void moveTo(Vector3f location) {
        if(client != null) {
            if(!lastMoveToLocation.equals(location)) {
                lastMoveToLocation.set(location);
                sendMoveSync();
            }
        }
    }

    private void sendMoveSync() {
        client.send(new AutoControlMessage(entity_id, lastAimDirection, lastMoveToLocation));
    }

    @Override
    public abstract boolean isMoving();

    @Override
    public abstract Vector3f getTargetLocation();

    @Override
    public abstract Vector3f getLocation();

    @Override
    public abstract Vector3f getAimDirection();

    @Override
    public abstract void doPerformAction(int action, boolean activate);

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public abstract void setSpatial(Spatial spatial);

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
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
