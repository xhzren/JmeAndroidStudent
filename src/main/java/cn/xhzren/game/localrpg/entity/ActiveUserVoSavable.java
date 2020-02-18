package cn.xhzren.game.localrpg.entity;

import com.jme3.export.*;

import java.io.IOException;

public class ActiveUserVoSavable implements Savable {
    @Override
    public void read(JmeImporter im) throws IOException{
        InputCapsule capsule = im.getCapsule(this);
        name = capsule.readString("name", "");
sex = capsule.readString("sex", "");

    }
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(name, "name", "");
capsule.write(sex, "sex", "");

    }

    
private String name;

private String sex;

 }
