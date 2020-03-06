package cn.xhzren.netty.controls;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterTestControl extends AbstractControl {

    static Logger logger = LoggerFactory.getLogger(CharacterTestControl.class);

    @Override
    protected void controlUpdate(float tpf) {

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    private ActionListener playerActionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            logger.info("action : name-> {},isPressed-> {}", name, isPressed);
        }
    };
    private AnalogListener playerAnalogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
             }
    };

    public ActionListener getActionListener() {
        return playerActionListener;
    }

    public AnalogListener getAnalogListener() {
        return playerAnalogListener;
    }
}
