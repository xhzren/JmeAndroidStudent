package cn.xhzren.netty.entity.input;

import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;

public class MyKeyInput implements KeyInput {

    public static Integer valueOf(String name) {
        try {
            return MyKeyInput.class.getField(name).getInt(new MyKeyInput());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void setInputListener(RawInputListener listener) {

    }

    @Override
    public long getInputTimeNanos() {
        return 0;
    }
}

