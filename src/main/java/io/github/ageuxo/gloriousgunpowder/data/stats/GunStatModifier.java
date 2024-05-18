package io.github.ageuxo.gloriousgunpowder.data.stats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GunRegistries;

public record GunStatModifier(GunStat gunStat, Type type, double value) {

    public static final Codec<GunStatModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GunRegistries.GUN_STATS.byNameCodec().fieldOf("stat").forGetter(GunStatModifier::gunStat),
            Codec.STRING.fieldOf("type").forGetter(GunStatModifier::typeId),
            Codec.DOUBLE.fieldOf("value").forGetter(GunStatModifier::value)
    ).apply(instance, GunStatModifier::new));

    public GunStatModifier(GunStat gunStat, String typeId, double value){
        this(gunStat, Type.valueOf(typeId),  value);
    }

    public String typeId(){
        return type.name();
    }

    public enum Type{
        ADDITION,
        MULTIPLIER

    }
}
