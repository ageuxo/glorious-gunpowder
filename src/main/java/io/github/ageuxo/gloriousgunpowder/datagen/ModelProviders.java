package io.github.ageuxo.gloriousgunpowder.datagen;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.item.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.data.PackOutput;
import software.bernie.geckolib.renderer.GeckolibSpecialRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModelProviders extends ModelProvider {
    public ModelProviders(PackOutput output) {
        super(output, GloriousGunpowderMod.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.itemModelOutput.accept(ModItems.BULLET_ITEM.get(), ItemModelUtils.plainModel(modLocation("item/bullet")));
        itemModels.itemModelOutput.accept(ModItems.BASIC_FIREARM.get(), ItemModelUtils.plainModel(modLocation("item/basic_firearm")));

        registerSpecialItemRenderers(itemModels.itemModelOutput);
    }

    protected void registerSpecialItemRenderers(ItemModelOutput output) {
        output.accept(ModItems.FOUR_PART_GUN.get(),
                ItemModelUtils.specialModel(modLocation("null"), new GeckolibSpecialRenderer.Unbaked()));
    }
}
