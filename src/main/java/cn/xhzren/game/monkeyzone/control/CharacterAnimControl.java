package cn.xhzren.game.monkeyzone.control;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;

/**
 * Handles animation of character
 * 角色动画处理器
 * @author normenhansen
 */
public class CharacterAnimControl implements Control {

    protected boolean enabled =true;
    protected Spatial spatial;
    protected AnimControl animControl;
    protected CharacterControl characterControl;
    protected AnimChannel torsoChannel;
    protected AnimChannel feetChannel;

    public CharacterAnimControl() {}

    @Override
    public void setSpatial(Spatial spatial) {
        if(spatial == null) {
            return;
        }
        animControl = spatial.getControl(AnimControl.class);
        characterControl = spatial.getControl(CharacterControl.class);
        if(animControl != null && characterControl != null) {
            enabled = true;
            torsoChannel = animControl.createChannel();
            feetChannel = animControl.createChannel();
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void update(float tpf) {
        if(!enabled) {
            return;
        }
        if(!characterControl.onGround()) {
            if(!"JumpLoop".equals(torsoChannel.getAnimationName())) {
                torsoChannel.setAnim("JumpLoop");
            }
            if(!"JumpLoop".equals(feetChannel.getAnimationName())) {
                feetChannel.setAnim("JumpLoop");
            }
            return;
        }

        if(characterControl.getWalkDirection().length() > 0) {
            if(!"RunTop".equals(torsoChannel.getAnimationName())) {
                torsoChannel.setAnim("RunTop");
            }
            if(!"RunBase".equals(feetChannel.getAnimationName())) {
                feetChannel.setAnim("RunBase");
            }
        }else {
            if (!"IdleTop".equals(torsoChannel.getAnimationName()))
                torsoChannel.setAnim("IdleTop");
            if (!"IdleBase".equals(feetChannel.getAnimationName()))
                feetChannel.setAnim("IdleBase");
        }
    }

    @Override
    public void render(RenderManager renderManager, ViewPort viewPort) {
    }

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
