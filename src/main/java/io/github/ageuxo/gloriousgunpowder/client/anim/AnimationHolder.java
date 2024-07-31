package io.github.ageuxo.gloriousgunpowder.client.anim;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class AnimationHolder {

    private final ResourceLocation id;
    private final Map<String, Animation> animationMap;

    public AnimationHolder(ResourceLocation id, Map<String, Animation> animationMap) {
        this.id = id;
        this.animationMap = animationMap;
    }
}
