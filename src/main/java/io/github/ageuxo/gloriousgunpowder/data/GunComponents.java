package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record GunComponents(ResourceLocation material, ResourceLocation shape) {
    public static final Codec<GunComponents> CODEC = RecordCodecBuilder.create(
            cooldownInstance -> cooldownInstance.group(
                            ResourceLocation.CODEC.fieldOf("material").forGetter(GunComponents::material),
                            ResourceLocation.CODEC.fieldOf("part").forGetter(GunComponents::shape)
                    )
                    .apply(cooldownInstance, GunComponents::new)
    );
    public static final Codec<List<GunComponents>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, GunComponents> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            GunComponents::material,
            ResourceLocation.STREAM_CODEC,
            GunComponents::shape,
            GunComponents::new
    );
}
