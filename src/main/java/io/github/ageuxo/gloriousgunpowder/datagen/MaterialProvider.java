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
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

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
        context.register(IRON, new Material("iron",
                List.of(
                        new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.ADDITION, "iron_reload", 2),
                        new GunStatModifier(GunStats.DAMAGE.get(), GunStatModifier.Type.MULTIPLIER, "iron_damage", 1.5)
                )));
        context.register(WOOD, new Material("wood",
                List.of(
                        new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.MULTIPLIER, "wood_reload", 0.8),
                        new GunStatModifier(GunStats.DAMAGE.get(), GunStatModifier.Type.MULTIPLIER, "wood_damage", 0.8)
                )));
    }

    private static ResourceKey<Material> createKey(String name){
        return ResourceKey.create(Material.KEY, GloriousGunpowderMod.rl(name));
    }
}
