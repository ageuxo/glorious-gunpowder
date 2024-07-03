package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class AnimationData {
    public static final Codec<AnimationData> CODEC = Codec.unboundedMap(Codec.INT, KeyTransform.CODEC).xmap(AnimationData::new, AnimationData::keyFrameMap);
    private final Map<Integer, KeyTransform> keyFrameMap;
    ResourceLocation key;

    public AnimationData(Map<Integer, KeyTransform> keyFrameMap) {
        this.keyFrameMap = keyFrameMap;
    }

    public KeyTransform get(int frame){
        return keyFrameMap.get(frame);
    }

    public Map<Integer, KeyTransform> keyFrameMap(){
        return keyFrameMap;
    }

    public ResourceLocation key(){
        return this.key;
    }
}
