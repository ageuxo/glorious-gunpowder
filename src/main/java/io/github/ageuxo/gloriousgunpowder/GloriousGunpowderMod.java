package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.block.ModBlocks;
import io.github.ageuxo.gloriousgunpowder.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(GloriousGunpowderMod.MOD_ID)
public class GloriousGunpowderMod {
    public static final String MOD_ID = "glorious_gunpowder";

    public GloriousGunpowderMod(IEventBus eventBus) {
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        GunRegistries.register(eventBus);

        eventBus.register(ModBusEvents.class);
        if (FMLEnvironment.dist == Dist.CLIENT){
            eventBus.register(ClientModBusEvents.class);
        }
    }

    public static ResourceLocation rl(String path){
        return new ResourceLocation(MOD_ID, path);
    }

}
