package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;

import java.util.Map;

public record Animation(float length, Map<String, GroupAnimationData> data) {
    public static final Codec<Map<String, GroupAnimationData>> DATA_CODEC = Codec.unboundedMap(Codec.STRING, GroupAnimationData.CODEC);
    public static final Codec<Animation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("animation_length").forGetter(Animation::length),
            DATA_CODEC.fieldOf("bones").forGetter(Animation::data)
    ).apply(instance, Animation::new));
    public static final Animation EMPTY = new Animation(0, Map.of());

    public GroupAnimationData getGroupData(BoneGroup group) {
        return data.get(group.name());
    }

}
