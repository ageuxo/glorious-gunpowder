package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;

import java.util.Map;

public record Animation(String name, int length, Map<String, GroupAnimationData> data) {
    public static final Codec<Float> FLOAT_STRING_KEY = Codec.STRING.comapFlatMap(
            s -> {
                try {
                    return DataResult.success(Float.valueOf(s));
                } catch (NumberFormatException e) {
                    return DataResult.error(()->s + " is not a float");
                }
            },
            aFloat -> Float.toString(aFloat));
    public static final Codec<Map<String, GroupAnimationData>> DATA_CODEC = Codec.unboundedMap(Codec.STRING, GroupAnimationData.CODEC);
    public static final Codec<Integer> INTEGER_CODEC = FLOAT_STRING_KEY.xmap(f -> Math.round(f * 20), i ->(float) i / 20f);
    public static final Codec<Animation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Animation::name),
            INTEGER_CODEC.fieldOf("animation_length").forGetter(Animation::length),
            DATA_CODEC.fieldOf("bones").forGetter(Animation::data)
    ).apply(instance, Animation::new));
    public static final Animation EMPTY = new Animation("empty",0, Map.of());

    public Animation(String name, float length, Map<String, GroupAnimationData> data) {
        this(name, (int)(length*20), data);
    }

    public GroupAnimationData getGroupData(BoneGroup group) {
        return data.get(group.name());
    }

}
