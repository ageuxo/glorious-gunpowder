package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.client.render.GunRenderer;
import io.github.ageuxo.gloriousgunpowder.models.GunPartGeometryLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.Map;

public class ClientModBusEvents {
    public static final String MODEL_PATH_PREFIX = "models/"+GloriousGunpowderMod.MOD_ID;

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        GunRenderer.INSTANCE = new GunRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
    }

    @SubscribeEvent
    public static void modelRegistry(ModelEvent.RegisterAdditional event){
        FileToIdConverter modelFinder = FileToIdConverter.json(MODEL_PATH_PREFIX);
        Map<ResourceLocation, Resource> map = modelFinder.listMatchingResources(Minecraft.getInstance().getResourceManager());
        for (Map.Entry<ResourceLocation, Resource> entry : map.entrySet()){
            ResourceLocation location = entry.getKey();
            String path = location.getPath().replaceFirst("\\.json", "").replaceFirst("models/", "");
            ResourceLocation resourceLocation = new ResourceLocation(location.getNamespace(), path);
            GunRenderer.MODEL_LOCATIONS.add(resourceLocation);
            event.register(resourceLocation);
        }
    }

    @SubscribeEvent
    public static void geometryLoaders(ModelEvent.RegisterGeometryLoaders event){
        event.register(GunPartGeometryLoader.ID, GunPartGeometryLoader.INSTANCE);
    }
}
