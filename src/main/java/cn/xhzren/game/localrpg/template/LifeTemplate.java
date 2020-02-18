package cn.xhzren.game.localrpg.template;

import cn.xhzren.game.localrpg.entity.Life;
import cn.xhzren.game.localrpg.entity.SelfState;

public class LifeTemplate {

    public static Life HUMANOID(String name) {
        Life life = new Life(name, 10, 5f, 5f, SelfState.HEALTH);
        return life;
    }
    public static Life HUMANOID() {
        Life life = new Life(null, 10, 5f, 5f, SelfState.HEALTH);
        return life;
    }
}
