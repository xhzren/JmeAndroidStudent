package cn.xhzren.game.monkeyzone.message;

import cn.xhzren.game.monkeyzone.network.physicssync.PhysicsSyncMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;

/**
 * sets userdata in a client-side entity
 * 在客户端实体中设置用户数据
 * @author normenhansen
 */
@Serializable()
public class ServerEntityDataMessage extends PhysicsSyncMessage {

    public String name;
    public byte type;
    public int intData;
    public float floatData;
    public long longData;
    public boolean booleanData;
    public String stringData;

    public ServerEntityDataMessage() {
    }

    public ServerEntityDataMessage(long id, String name, Object value) {
        this.name = name;
        syncId = id;
        type = getObjectType(value);
        switch (type) {
            case 0:
                intData = (Integer) value;
                break;
            case 1:
                floatData = (Float) value;
                break;
            case 2:
                booleanData = (Boolean) value;
                break;
            case 3:
                stringData = (String) value;
                break;
            case 4:
                longData = (Long) value;
                break;
            default:
                throw new UnsupportedOperationException("Cannot apply wrong userdata type.");
        }
    }

    @Override
    public void appData(Object object) {
        Spatial spat = ((Spatial) object);
        switch (type) {
            case 0:
                spat.setUserData(name, intData);
                break;
            case 1:
                spat.setUserData(name, floatData);
                break;
            case 2:
                spat.setUserData(name, booleanData);
                break;
            case 3:
                spat.setUserData(name, stringData);
                break;
            case 4:
                spat.setUserData(name, longData);
                break;
            default:
                throw new UnsupportedOperationException("Cannot apply wrong userdata type.");
        }
    }

    private static byte getObjectType(Object type) {
        if (type instanceof Integer) {
            return 0;
        } else if (type instanceof Float) {
            return 1;
        } else if (type instanceof Boolean) {
            return 2;
        } else if (type instanceof String) {
            return 3;
        } else if (type instanceof Long) {
            return 4;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type.getClass().getName());
        }
    }
}
