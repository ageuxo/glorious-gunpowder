package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStatModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public record Material(String name, List<GunStatModifier> statModifiers) {
    public static final ResourceKey<Registry<Material>> KEY = ResourceKey.createRegistryKey(GloriousGunpowderMod.rl("material"));
    public static final Codec<Material> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(Material::name),
                    GunStatModifier.CODEC.listOf().fieldOf("modifiers").forGetter(Material::statModifiers)
            ).apply(instance, Material::new));
}
