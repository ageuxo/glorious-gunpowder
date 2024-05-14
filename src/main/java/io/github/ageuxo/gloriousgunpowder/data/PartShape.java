package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record PartShape(String name, float damage, float damageMod, float equipSpeed, float equipMod, float reloadSpeed, float reloadMod) {
    public static final ResourceKey<Registry<PartShape>> KEY = ResourceKey.createRegistryKey(GloriousGunpowderMod.rl("part_shape"));
    public static final Codec<PartShape> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(PartShape::name),
                    Codec.FLOAT.fieldOf("damage").forGetter(PartShape::damage),
                    Codec.FLOAT.fieldOf("damageMod").forGetter(PartShape::damageMod),
                    Codec.FLOAT.fieldOf("equipSpeed").forGetter(PartShape::equipSpeed),
                    Codec.FLOAT.fieldOf("equipMod").forGetter(PartShape::equipMod),
                    Codec.FLOAT.fieldOf("reloadSpeed").forGetter(PartShape::reloadSpeed),
                    Codec.FLOAT.fieldOf("reloadMod").forGetter(PartShape::reloadMod)
            ).apply(instance, PartShape::new));
}
