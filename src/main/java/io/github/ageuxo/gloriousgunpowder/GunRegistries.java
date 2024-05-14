package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.data.Material;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GunRegistries {
    public static final DeferredRegister<PartShape> PART_SHAPES = DeferredRegister.create(PartShape.KEY, GloriousGunpowderMod.MOD_ID);
    public static final  DeferredRegister<Material> MATERIALS = DeferredRegister.create(Material.KEY, GloriousGunpowderMod.MOD_ID);

    public static void register(IEventBus bus){
        PART_SHAPES.register(bus);
        MATERIALS.register(bus);
    }
}
