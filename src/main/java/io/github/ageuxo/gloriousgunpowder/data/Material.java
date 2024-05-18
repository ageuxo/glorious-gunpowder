package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStatModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagEntry;

import java.util.List;

public record Material(ResourceKey<Material> key, List<Entry> entries) {
    public static final ResourceKey<Registry<Material>> KEY = ResourceKey.createRegistryKey(GloriousGunpowderMod.rl("material"));
    public static final Codec<Material> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(KEY).fieldOf("key").forGetter(Material::key),
                    Entry.CODEC.listOf().fieldOf("entries").forGetter(Material::entries)
            ).apply(instance, Material::new));

    public record Entry(TagEntry tagEntry, List<GunStatModifier> modifiers) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TagEntry.CODEC.fieldOf("id").forGetter(Entry::tagEntry),
                GunStatModifier.CODEC.listOf().fieldOf("modifiers").forGetter(Entry::modifiers)
        ).apply(instance, Entry::new));
    }
}
