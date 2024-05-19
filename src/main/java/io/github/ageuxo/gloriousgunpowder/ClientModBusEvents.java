package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import io.github.ageuxo.gloriousgunpowder.models.GunPartGeometryLoader;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.function.Supplier;

public class ClientModBusEvents {
    public static final String MODEL_PATH_PREFIX = "/"+GloriousGunpowderMod.MOD_ID+"/parts/";

    @SubscribeEvent
    public static void modelRegistry(ModelEvent.RegisterAdditional event){
        Res
    }

    @SubscribeEvent
    public static void geometryLoaders(ModelEvent.RegisterGeometryLoaders event){
        event.register(GunPartGeometryLoader.ID, GunPartGeometryLoader.INSTANCE);
    }
}
