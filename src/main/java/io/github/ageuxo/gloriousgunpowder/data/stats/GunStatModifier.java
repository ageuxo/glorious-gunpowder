package io.github.ageuxo.gloriousgunpowder.data.stats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GunRegistries;
import net.minecraft.core.Holder;

public record GunStatModifier(GunStat gunStat, Type type, String id, double value) {

    public static final Codec<GunStatModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GunRegistries.GUN_STATS.byNameCodec().fieldOf("stat").forGetter(GunStatModifier::gunStat),
            Codec.INT.fieldOf("type").forGetter(GunStatModifier::typeId),
            Codec.STRING.fieldOf("id").forGetter(GunStatModifier::id),
            Codec.DOUBLE.fieldOf("value").forGetter(GunStatModifier::value)
    ).apply(instance, GunStatModifier::new));

    public GunStatModifier(GunStat gunStat, int typeOrdinal, String id, double value){
        this(gunStat, Type.fromOrdinal(typeOrdinal), id, value);
    }

    public int typeId(){
        return type.ordinal();
    }

    public enum Type{
        ADDITION,
        MULTIPLIER;

        private static final Type[] VALUES = Type.values();

        public static Type fromOrdinal(int ordinal){
            return VALUES[ordinal];
        }

    }
}
