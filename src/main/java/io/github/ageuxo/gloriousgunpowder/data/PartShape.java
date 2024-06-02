package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStatModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.Optional;

public record PartShape(ResourceKey<PartShape> key, List<GunStatModifier> statModifiers) {
    public static final ResourceKey<Registry<PartShape>> KEY = ResourceKey.createRegistryKey(GloriousGunpowderMod.rl("part_shape"));
    public static final Codec<PartShape> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(KEY).fieldOf("key").forGetter(PartShape::key),
                    GunStatModifier.CODEC.listOf().fieldOf("modifiers").forGetter(PartShape::statModifiers)
            ).apply(instance, PartShape::new));

    public boolean is(RegistryAccess registryAccess, TagKey<PartShape> tagKey){
        Registry<PartShape> registry = registryAccess.registryOrThrow(KEY);
        Optional<HolderSet.Named<PartShape>> optionalHolderSet = registry.getTag(tagKey);
        Optional<Holder.Reference<PartShape>> optionalPartShapeHolder = registry.getHolder(this.key);
        if (optionalHolderSet.isPresent()){
            HolderSet.Named<PartShape> holderSet = optionalHolderSet.get();
            if (optionalPartShapeHolder.isPresent()){
                Holder.Reference<PartShape> partShapeHolder = optionalPartShapeHolder.get();
                return holderSet.contains(partShapeHolder);
            }
         }
        return false;
    }
}
