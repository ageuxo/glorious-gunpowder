package io.github.ageuxo.gloriousgunpowder.client;

import io.github.ageuxo.gloriousgunpowder.client.model.GroupGeometryLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.Map;

public class ClientModBusEvents {
    private static final String MODEL_PATH_PREFIX = "models/gun_part";

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event){
        FileToIdConverter modelFinder = FileToIdConverter.json(MODEL_PATH_PREFIX);
        Map<ResourceLocation, Resource> map = modelFinder.listMatchingResources(Minecraft.getInstance().getResourceManager());
        for (Map.Entry<ResourceLocation, Resource> entry : map.entrySet()){
            ResourceLocation location = entry.getKey();
            String path = location.getPath().replaceFirst("\\.json", "").replaceFirst("models/", "");
            ResourceLocation resourceLocation = new ResourceLocation(location.getNamespace(), path);
            event.register(resourceLocation);
        }
    }

    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event){
        event.register(GroupGeometryLoader.ID, GroupGeometryLoader.INSTANCE);
    }
}
