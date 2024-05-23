package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.block.ModBlocks;
import io.github.ageuxo.gloriousgunpowder.client.render.GunRenderHandler;
import io.github.ageuxo.gloriousgunpowder.client.sound.ModSounds;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.entity.ModEntities;
import io.github.ageuxo.gloriousgunpowder.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(GloriousGunpowderMod.MOD_ID)
public class GloriousGunpowderMod {
    public static final String MOD_ID = "glorious_gunpowder";

    public GloriousGunpowderMod(IEventBus eventBus) {
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModEntities.register(eventBus);
        ModSounds.register(eventBus);
        GunRegistries.register(eventBus);
        GunDataComponents.register(eventBus);
        eventBus.register(ModBusEvents.class);
        if(FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.register(new GunRenderHandler());
        }
    }

    public static ResourceLocation rl(String path){
        return new ResourceLocation(MOD_ID, path);
    }

}
