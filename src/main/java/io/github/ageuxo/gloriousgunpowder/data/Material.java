package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record Material(String name, float damage, float equipSpeed, float reloadSpeed) {
    public static final ResourceKey<Registry<Material>> KEY = ResourceKey.createRegistryKey(GloriousGunpowderMod.rl("material"));
    public static final Codec<Material> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(Material::name),
                    Codec.FLOAT.fieldOf("damage").forGetter(Material::damage),
                    Codec.FLOAT.fieldOf("equipSpeed").forGetter(Material::equipSpeed),
                    Codec.FLOAT.fieldOf("reloadSpeed").forGetter(Material::reloadSpeed)
            ).apply(instance, Material::new));
}
