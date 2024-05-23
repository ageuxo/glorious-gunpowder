package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.models.GunPartGeometryLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.Map;

public class ClientModBusEvents {
    public static final String MODEL_PATH_PREFIX = GloriousGunpowderMod.MOD_ID+"/models";

    @SubscribeEvent
    public static void modelRegistry(ModelEvent.RegisterAdditional event){
        FileToIdConverter modelFinder = FileToIdConverter.json(MODEL_PATH_PREFIX);
        Map<ResourceLocation, Resource> map = modelFinder.listMatchingResources(Minecraft.getInstance().getResourceManager());
        for (Map.Entry<ResourceLocation, Resource> entry : map.entrySet()){
            event.register(entry.getKey());
        }
    }

    @SubscribeEvent
    public static void geometryLoaders(ModelEvent.RegisterGeometryLoaders event){
        event.register(GunPartGeometryLoader.ID, GunPartGeometryLoader.INSTANCE);
    }
}
