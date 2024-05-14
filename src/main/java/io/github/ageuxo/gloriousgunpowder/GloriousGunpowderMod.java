package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.block.ModBlocks;
import io.github.ageuxo.gloriousgunpowder.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(GloriousGunpowderMod.MOD_ID)
public class GloriousGunpowderMod {
    public static final String MOD_ID = "glorious_gunpowder";

    public GloriousGunpowderMod(IEventBus eventBus) {
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        GunRegistries.register(eventBus);

        eventBus.register(ModBusEvents.class);
    }

    public static ResourceLocation rl(String path){
        return new ResourceLocation(MOD_ID, path);
    }

}
