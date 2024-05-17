package io.github.ageuxo.gloriousgunpowder.datagen;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
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

public class PartShapeProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(PartShape.KEY, PartShapeProvider::bootstrap);

    public PartShapeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(GloriousGunpowderMod.MOD_ID));
    }

    @Override
    public @NotNull String getName() {
        return "PartShape";
    }

    public static final ResourceKey<PartShape> STANDARD_STOCK = createKey("standard_stock");
    public static final ResourceKey<PartShape> UNRIFLED_BARREL = createKey("unrifled_barrel");
    public static final ResourceKey<PartShape> MATCHLOCK = createKey("matchlock");

    public static void bootstrap(BootstrapContext<PartShape> context){
        context.register(STANDARD_STOCK, new PartShape("standard_stock",
                List.of(
                        new GunStatModifier(GunStats.ACCURACY.get(), GunStatModifier.Type.MULTIPLIER, "stock_accuracy", 1.2),
                        new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.ADDITION, "stock_reload", -1)
        )));
        context.register(UNRIFLED_BARREL, new PartShape("unrifled_barrel",
                List.of(
                        new GunStatModifier(GunStats.ACCURACY.get(), GunStatModifier.Type.MULTIPLIER, "barrel_accuracy", 0.4),
                        new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.ADDITION, "barrel_reload", -3)
                )));
        context.register(MATCHLOCK, new PartShape("matchlock",
                List.of(
                        new GunStatModifier(GunStats.DAMAGE.get(), GunStatModifier.Type.MULTIPLIER, "matchlock_damage", 0.8),
                        new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.ADDITION, "matchlock_reload", 2)
                )));
    }

    private static ResourceKey<PartShape> createKey(String name){
        return ResourceKey.create(PartShape.KEY, GloriousGunpowderMod.rl(name));
    }
}
