package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record KeyTransform(Vector3f position, Vector3f scale, Quaternionf rotation) {
    public static final Codec<KeyTransform> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.VECTOR3F.fieldOf("position").forGetter(KeyTransform::position),
            ExtraCodecs.VECTOR3F.fieldOf("scale").forGetter(KeyTransform::scale),
            ExtraCodecs.QUATERNIONF.fieldOf("rotation").forGetter(KeyTransform::rotation)
    ).apply(instance, KeyTransform::new));
}
