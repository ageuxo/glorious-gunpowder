package io.github.ageuxo.gloriousgunpowder.datagen;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.Material;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MaterialProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Material.KEY, MaterialProvider::bootstrap);

    public MaterialProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(GloriousGunpowderMod.MOD_ID));
    }

    public static void bootstrap(BootstrapContext<Material> context){

    }

    @Override
    public @NotNull String getName() {
        return "Material";
    }
}
