package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
//TODO: replace with something even more better
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
