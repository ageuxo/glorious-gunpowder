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
        context.register(STANDARD_STOCK, new PartShape(STANDARD_STOCK,
                List.of(
                        new GunStatModifier(GunStats.ACCURACY.get(), GunStatModifier.Type.MULTIPLIER, 1.2f),
                        new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.ADDITION, -1f),
                        new GunStatModifier(GunStats.RECOIL.get(), GunStatModifier.Type.ADDITION, -1f)
        )));
        context.register(UNRIFLED_BARREL, new PartShape(UNRIFLED_BARREL,
                List.of(
                        new GunStatModifier(GunStats.ACCURACY.get(), GunStatModifier.Type.MULTIPLIER, 0.4f),
                        new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.ADDITION, -3f)
                )));
        context.register(MATCHLOCK, new PartShape(MATCHLOCK,
                List.of(
                        new GunStatModifier(GunStats.DAMAGE.get(), GunStatModifier.Type.MULTIPLIER, 0.8f),
                        new GunStatModifier(GunStats.RELOAD_TIME.get(), GunStatModifier.Type.ADDITION, 2f)
                )));
    }

    private static ResourceKey<PartShape> createKey(String name){
        return ResourceKey.create(PartShape.KEY, GloriousGunpowderMod.rl(name));
    }
}
