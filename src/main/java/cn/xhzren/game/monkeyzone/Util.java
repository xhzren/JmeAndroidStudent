package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.message.*;
import com.jme3.network.serializing.Serializer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author normenhansen
 */
public class Util {

    public static void setLogLevels(boolean debug) {
        if(debug) {
            Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
            Logger.getLogger("de.lessvoid.nifty.effects.EffectProcessor").setLevel(Level.SEVERE);
            Logger.getLogger("org.lwjgl").setLevel(Level.WARNING);
            Logger.getLogger("com.jme3").setLevel(Level.FINEST);
            Logger.getLogger("cn.xhzren.game.monkeyzone").setLevel(Level.FINEST);
        } else {
            Logger.getLogger("de.lessvoid").setLevel(Level.WARNING);
            Logger.getLogger("de.lessvoid.nifty.effects.EffectProcessor").setLevel(Level.WARNING);
            Logger.getLogger("org.lwjgl").setLevel(Level.WARNING);
            Logger.getLogger("com.jme3").setLevel(Level.WARNING);
            Logger.getLogger("cn.xhzren.game.monkeyzone").setLevel(Level.WARNING);
        }
    }

    public static void registerSerializers() {
        Serializer.registerClass(ActionMessage.class);
        Serializer.registerClass(AutoControlMessage.class);
        Serializer.registerClass(ChatMessage.class);
        Serializer.registerClass(ClientJoinMessage.class);
        Serializer.registerClass(HandshakeMessage.class);
        Serializer.registerClass(ManualControlMessage.class);
        Serializer.registerClass(ServerAddEntityMessage.class);
        Serializer.registerClass(ServerAddPlayerMessage.class);
        Serializer.registerClass(SyncCharacterMessage.class);
//        Serializer.registerClass(ServerEffectMessage.class);
        Serializer.registerClass(ServerEnableEntityMessage.class);
        Serializer.registerClass(ServerDisableEntityMessage.class);
        Serializer.registerClass(ServerEnterEntityMessage.class);
//        Serializer.registerClass(ServerEntityDataMessage.class);
        Serializer.registerClass(ServerJoinMessage.class);
        Serializer.registerClass(SyncRigidBodyMessage.class);
//        Serializer.registerClass(ServerPlayerDataMessage.class);
//        Serializer.registerClass(ServerRemoveEntityMessage.class);
//        Serializer.registerClass(ServerRemovePlayerMessage.class);
        Serializer.registerClass(StartGameMessage.class);
    }
}
