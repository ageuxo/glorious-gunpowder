package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStatModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public record PartShape(ResourceKey<PartShape> key, List<GunStatModifier> statModifiers) {
    public static final ResourceKey<Registry<PartShape>> KEY = ResourceKey.createRegistryKey(GloriousGunpowderMod.rl("part_shape"));
    public static final Codec<PartShape> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(KEY).fieldOf("key").forGetter(PartShape::key),
                    GunStatModifier.CODEC.listOf().fieldOf("modifiers").forGetter(PartShape::statModifiers)
            ).apply(instance, PartShape::new));
}
