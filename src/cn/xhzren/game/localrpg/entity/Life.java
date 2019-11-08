package cn.xhzren.game.localrpg.entity;

import com.jme3.export.*;

import java.io.IOException;
import java.util.List;

public class Life implements Savable {
    @Override
    public void read(JmeImporter im) throws IOException{
        InputCapsule capsule = im.getCapsule(this);
        name = capsule.readString("name", "");
        hp = capsule.readInt("hp", 0);
        attack = capsule.readFloat("attack", 1F);
        defense = capsule.readFloat("defense", 1F);
        selfState = capsule.readEnum("selfState", SelfState.class, SelfState.HEALTH);
        nameArray = capsule.readStringArray("nameArray", null);
        nameArray2D = capsule.readStringArray2D("nameArray2D", null);

    }
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(name, "name", null);
        capsule.write(hp, "hp", 0);
        capsule.write(attack, "attack", 0);
        capsule.write(defense, "defense", 0);
        capsule.write(selfState, "selfState", null);
        capsule.write(nameArray, "nameArray", null);
        capsule.write(nameArray2D, "nameArray2D", null);
    }

    private String name;
    private String[] nameArray;
    private String[][] nameArray2D;
    private Integer hp;
//    private Integer[] hpArray;
//    private Integer[][] hpArray2D;
    private Float attack;
//    private float[] attackArray;
//    private float[][] attackArray2D;
//    private Life savable;
//    private Life[] savableArray;
//    private Life[][] savableArray2D;
//    private List<Life> savableArrayList;
//    private List<Life>[] savableArrayListArray;
//    private List<Life>[][] savableArrayListArray2D;
    private Float defense;
    private SelfState selfState;

    public Life() {
    }

    @Override
    public String toString() {
        return "Life{" +
                "name='" + name + '\'' +
                ", hp=" + hp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", selfState=" + selfState +
                '}';
    }

    public Life(String name, Integer hp, Float attack, Float defense, SelfState selfState) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.selfState = selfState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Float getAttack() {
        return attack;
    }

    public void setAttack(Float attack) {
        this.attack = attack;
    }

    public Float getDefense() {
        return defense;
    }

    public void setDefense(Float defense) {
        this.defense = defense;
    }

    public SelfState getSelfState() {
        return selfState;
    }

    public void setSelfState(SelfState selfState) {
        this.selfState = selfState;
    }


}
