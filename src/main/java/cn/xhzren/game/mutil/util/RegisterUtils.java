package cn.xhzren.game.mutil.util;

import cn.xhzren.game.mutil.message.ConnectionMessage;
import cn.xhzren.game.mutil.message.HelloMessage;
import com.jme3.network.serializing.Serializer;

public class RegisterUtils {

    public static void initSerializable() {
        Serializer.registerClass(HelloMessage.class);
        Serializer.registerClass(ConnectionMessage.class);
    }
}
