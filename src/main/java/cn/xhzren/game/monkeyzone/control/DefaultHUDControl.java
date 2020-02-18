package cn.xhzren.game.monkeyzone.control;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;

import java.io.IOException;

public class DefaultHUDControl implements HUDControl {

    protected boolean enabled = true;
    protected Spatial spatial;
    protected Screen screen;
    protected float updateTime = 0.25f;
    protected float curTime;
    protected TextRenderer hitPoints;
    protected TextRenderer speed;
    protected TextRenderer vehicle;

    public DefaultHUDControl(Screen screen) {
        this.screen = screen;
        if(screen == null) {
            throw new IllegalStateException("DefaultHUDControl niffy screen null1");
        }

    }


    @Override
    public void setSpatial(Spatial spatial) {
        if(spatial == null) {
            this.spatial = spatial;
            return;
        }
        this.spatial = spatial;
    }

    @Override
    public void update(float tpf) {
        if(!enabled) {
            return;
        }

        curTime += tpf;
        if(curTime > updateTime) {
            curTime = 0;
            Float hitPoints = (Float)spatial.getUserData("HitPoints");
            Float speed = (Float)spatial.getUserData("Speed");
            if(hitPoints != null) {
                this.hitPoints.setText("HP: " + hitPoints);
            }else {
                this.hitPoints.setText("No HitPoints!");
            }
            if(speed != null) {
                this.speed.setText("Speed: " + speed);
            }else {
                this.speed.setText("No HitPoints!");
            }
        }
    }

    @Override
    public void render(RenderManager renderManager, ViewPort viewPort) {

    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
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
