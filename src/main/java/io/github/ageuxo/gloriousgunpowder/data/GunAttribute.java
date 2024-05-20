package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GunAttribute(float damage, float accuracy, float reloadSpeed, float velocity, float recoil) {
    public static final Codec<GunAttribute> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("damage").forGetter(GunAttribute::damage),
                    Codec.FLOAT.fieldOf("accuracy").forGetter(GunAttribute::accuracy),
                    Codec.FLOAT.fieldOf("reloadSpeed").forGetter(GunAttribute::reloadSpeed),
                    Codec.FLOAT.fieldOf("velocity").forGetter(GunAttribute::velocity),
                    Codec.FLOAT.fieldOf("recoil").forGetter(GunAttribute::recoil)
            ).apply(instance, GunAttribute::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GunAttribute> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            GunAttribute::damage,
            ByteBufCodecs.FLOAT,
            GunAttribute::accuracy,
            ByteBufCodecs.FLOAT,
            GunAttribute::reloadSpeed,
            ByteBufCodecs.FLOAT,
            GunAttribute::velocity,
            ByteBufCodecs.FLOAT,
            GunAttribute::recoil,
            GunAttribute::new

    );
}
