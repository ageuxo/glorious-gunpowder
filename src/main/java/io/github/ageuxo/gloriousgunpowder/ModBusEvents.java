package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.data.Material;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class ModBusEvents {
    @SubscribeEvent
    public static void onRegisterDatapacks(DataPackRegistryEvent.NewRegistry event){
        event.dataPackRegistry(PartShape.KEY, PartShape.CODEC, PartShape.CODEC);
        event.dataPackRegistry(Material.KEY, Material.CODEC, Material.CODEC);
    }
}
