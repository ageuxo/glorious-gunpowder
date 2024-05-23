package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GunRegistries;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStat;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStatModifier;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStatsMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record GunAttribute(String stat, double value) {
    public static final Codec<GunAttribute> CODEC = RecordCodecBuilder.create(instance ->

            instance.group(
                    Codec.STRING.fieldOf("stat").forGetter(GunAttribute::stat),
                    Codec.DOUBLE.fieldOf("value").forGetter(GunAttribute::value)
            ).apply(instance, GunAttribute::new));
    public static final Codec<List<GunAttribute>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, GunAttribute> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            GunAttribute::stat,
            ByteBufCodecs.DOUBLE,
            GunAttribute::value,
            GunAttribute::new
    );
}
