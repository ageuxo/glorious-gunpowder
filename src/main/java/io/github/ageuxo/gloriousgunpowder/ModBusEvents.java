package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.data.Material;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import io.github.ageuxo.gloriousgunpowder.datagen.ItemTagProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.MaterialProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.ModBlockTagsProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.PartShapeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.concurrent.CompletableFuture;

public class ModBusEvents {
    @SubscribeEvent
    public static void onRegisterDatapacks(DataPackRegistryEvent.NewRegistry event){
        event.dataPackRegistry(PartShape.KEY, PartShape.CODEC, PartShape.CODEC);
        event.dataPackRegistry(Material.KEY, Material.CODEC, Material.CODEC);
    }

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event){
        event.register(GunRegistries.GUN_STATS);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        ModBlockTagsProvider blockTagsProvider =  new ModBlockTagsProvider(output, lookup, helper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ItemTagProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(true, new MaterialProvider(output, lookup));
        generator.addProvider(true, new PartShapeProvider(output, lookup));
    }
}
