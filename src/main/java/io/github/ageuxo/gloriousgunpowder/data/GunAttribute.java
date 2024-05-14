package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GunAttribute(float damage, float equipSpeed, float reloadSpeed) {
    public static final Codec<GunAttribute> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("damage").forGetter(GunAttribute::damage),
                    Codec.FLOAT.fieldOf("equipSpeed").forGetter(GunAttribute::equipSpeed),
                    Codec.FLOAT.fieldOf("reloadSpeed").forGetter(GunAttribute::reloadSpeed)
            ).apply(instance, GunAttribute::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GunAttribute> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            GunAttribute::damage,
            ByteBufCodecs.FLOAT,
            GunAttribute::equipSpeed,
            ByteBufCodecs.FLOAT,
            GunAttribute::reloadSpeed,
            GunAttribute::new
    );
}
