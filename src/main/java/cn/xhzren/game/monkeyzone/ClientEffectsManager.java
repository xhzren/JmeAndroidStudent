package cn.xhzren.game.monkeyzone;

import cn.xhzren.game.monkeyzone.data.EmitterData;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Plays effects on the client, uses lists to cache effects for fast display,
 * TODO: allow limiting effects count created in lists/world.
 * 在Client中播放特效，使用列表缓存以快速显示
 * @author normenhansen
 */
public class ClientEffectsManager extends AbstractAppState {

    private AssetManager assetManager;
    private AudioRenderer audioRenderer;
    private WorldManager worldManager;
    private HashMap<String, LinkedList<Node>> emitters = new HashMap<>();
    private HashMap<Long, EmitterData> liveEmitters = new HashMap<>();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.assetManager = app.getAssetManager();
        audioRenderer = app.getAudioRenderer();
        worldManager = app.getStateManager().getState(WorldManager.class);
    }

    public void playEffect(long id, String effectName, Vector3f location, Vector3f endLocation,
                           Quaternion rotation, Quaternion endRotation, float timer) {
        if(liveEmitters.containsKey(id)) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Trying to add effect with existing id");
            return;
        }
        Node effect = getEffect(effectName);
        EmitterData data = new EmitterData(effect, id, effectName, location,endLocation, rotation,endRotation,timer);
        effect.setLocalTranslation(location);
        effect.setLocalRotation(rotation);
        List<Spatial> children = effect.getChildren();
        for (Iterator<Spatial> it = children.iterator();it.hasNext();) {
            Spatial spat = it.next();
            if(spat instanceof ParticleEmitter) {
                ParticleEmitter emitter = (ParticleEmitter)spat;
                emitter.emitAllParticles();
            }else if (spat instanceof AudioNode){
                AudioNode audioNode = (AudioNode)spat;
                audioRenderer.playSource(audioNode);
            }
        }
        worldManager.getWorldRoot().attachChild(effect);
        if(id == -1) {
            putToNew(data);
        }else {
            data.id = id;
            liveEmitters.put(id, data);
        }
    }

    public void stopEffect(long id) {
        EmitterData data = liveEmitters.get(id);
        data.emit.removeFromParent();
        emitters.get(data.effectName).add(data.emit);
        List<Spatial> children = data.emit.getChildren();
        for (Iterator<Spatial> it = children.iterator();it.hasNext();) {
            Spatial spatial = it.next();
            if(spatial instanceof ParticleEmitter) {
                ((ParticleEmitter)spatial).killAllParticles();
            }else if(spatial instanceof AudioNode) {
                audioRenderer.stopSource((AudioNode)spatial);
            }
        }
    }

    private long putToNew(EmitterData data) {
        long id = 0;
        while (liveEmitters.containsKey(id)) {
            id++;
        }
        data.id = id;
        liveEmitters.put(id, data);
        return id;
    }

    private Node getEffect(String effectName) {
        if(emitters.get(effectName) == null) {
            emitters.put(effectName, new LinkedList<Node>());
        }
        Node emit = emitters.get(effectName).poll();
        if(emit == null) {
            emit = (Node)assetManager.loadModel(effectName);
        }
        return emit;

    }

    @Override
    public void update(float tpf) {
        //TODO: moving effects
        for (Iterator<Map.Entry<Long, EmitterData>> it = liveEmitters.entrySet().iterator();it.hasNext();) {
            Map.Entry<Long, EmitterData> entry = it.next();
            EmitterData data = entry.getValue();
            if(data.curTime >= data.timer) {
                stopEffect(data.id);
                emitters.get(data.effectName).add(data.emit);
                it.remove();
            }
            data.curTime += tpf;
        }
    }
}
