package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.data.Material;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStat;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStats;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class GunRegistries {
    public static final Registry<GunStat> GUN_STATS = new RegistryBuilder<>(GunStat.KEY).sync(true).create();
    public static final DeferredRegister<PartShape> PART_SHAPES = DeferredRegister.create(PartShape.KEY, GloriousGunpowderMod.MOD_ID);
    public static final DeferredRegister<Material> MATERIALS = DeferredRegister.create(Material.KEY, GloriousGunpowderMod.MOD_ID);

    public static void register(IEventBus bus){
        PART_SHAPES.register(bus);
        MATERIALS.register(bus);
        GunStats.GUN_STATS.register(bus);
    }
}
