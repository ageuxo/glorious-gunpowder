package io.github.ageuxo.gloriousgunpowder.datagen;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.Material;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStatModifier;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStats;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MaterialProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Material.KEY, MaterialProvider::bootstrap);

    public MaterialProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(GloriousGunpowderMod.MOD_ID));
    }

    @Override
    public @NotNull String getName() {
        return "Material";
    }

    public static final ResourceKey<Material> IRON = createKey("iron");
    public static final ResourceKey<Material> WOOD = createKey("wood");

    public static void bootstrap(BootstrapContext<Material> context){
        builder(context, IRON)
                .addEntry(PartShapeProvider.STANDARD_STOCK)
                    .modifier(new GunStatModifier(GunStats.ACCURACY.get(), GunStatModifier.Type.ADDITION, 0.2f))
                    .modifier(new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.MULTIPLIER, 1.3f))
                    .modifier(new GunStatModifier(GunStats.RECOIL.get(), GunStatModifier.Type.MULTIPLIER, 0.8f))
                .end()
                .addEntry(PartShapeProvider.UNRIFLED_BARREL)
                    .modifier(new GunStatModifier(GunStats.DAMAGE.get(), GunStatModifier.Type.MULTIPLIER, 1.4f))
                    .modifier(new GunStatModifier(GunStats.ACCURACY.get(), GunStatModifier.Type.MULTIPLIER, 1.4f))
                .end()
                .build();

        builder(context, WOOD)
                .addEntry(PartShapeProvider.STANDARD_STOCK)
                    .modifier(new GunStatModifier(GunStats.ACCURACY.get(), GunStatModifier.Type.ADDITION, 0.4f))
                    .modifier(new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.MULTIPLIER, 0.3f))
                .modifier(new GunStatModifier(GunStats.RECOIL.get(), GunStatModifier.Type.MULTIPLIER, 0.9f))
                .end()
                .addEntry(PartShapeProvider.UNRIFLED_BARREL)
                    .modifier(new GunStatModifier(GunStats.DAMAGE.get(), GunStatModifier.Type.MULTIPLIER, 0.4f))
                    .modifier(new GunStatModifier(GunStats.ACCURACY.get(), GunStatModifier.Type.MULTIPLIER, 0.4f))
                .end()
                .build();

    }

    public static MaterialBuilder builder(BootstrapContext<Material> context, ResourceKey<Material> key){
        return new MaterialBuilder(context, key);
    }

    public static ResourceKey<Material> createKey(String name){
        return ResourceKey.create(Material.KEY, GloriousGunpowderMod.rl(name));
    }

    public static class MaterialBuilder {
        private final BootstrapContext<Material> context;
        private final ResourceKey<Material> key;
        private final List<Material.Entry> entries = new ArrayList<>();

        public MaterialBuilder(BootstrapContext<Material> context, ResourceKey<Material> key) {
            this.context = context;
            this.key = key;
        }

        public EntryBuilder addEntry(TagKey<PartShape> tagKey){
            return addEntry(TagEntry.tag(tagKey.location()));
        }

        public EntryBuilder addEntry(ResourceKey<PartShape> key){
            return addEntry(TagEntry.element(key.location()));
        }

        private EntryBuilder addEntry(TagEntry tagEntry){
            return new EntryBuilder(this, tagEntry);
        }

        @SuppressWarnings("UnusedReturnValue")
        public Material build(){
            Material material = new Material(this.key, this.entries);
            context.register(this.key, material);
            return material;
        }
    }

    public static class EntryBuilder {
        private final MaterialBuilder parent;
        private final TagEntry tagEntry;
        private final List<GunStatModifier> modifiers = new ArrayList<>();

        public EntryBuilder(MaterialBuilder parent, TagEntry tagEntry) {
            this.parent = parent;
            this.tagEntry = tagEntry;
        }

        public EntryBuilder modifier(GunStatModifier modifier){
            this.modifiers.add(modifier);
            return this;
        }

        public MaterialBuilder end(){
            parent.entries.add(new Material.Entry(this.tagEntry, this.modifiers));
            return parent;
        }
    }
}
