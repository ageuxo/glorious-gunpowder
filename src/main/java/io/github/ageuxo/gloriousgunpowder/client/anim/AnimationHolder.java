package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.Map;

public class AnimationHolder {

    private final ResourceLocation id;
    private final Map<String, Animation> animationMap;

    public static final Codec<Map<String, Animation>> MAP_CODEC = ExtraCodecs.strictUnboundedMap(Codec.STRING, Animation.CODEC);

    public AnimationHolder(ResourceLocation id, Map<String, Animation> animationMap) {
        this.id = id;
        this.animationMap = animationMap;
    }

    public Animation get(String animation){
        return this.animationMap.get(animation);
    }

    public ResourceLocation id(){
        return this.id;
    }
}
