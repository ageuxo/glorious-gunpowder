package io.github.ageuxo.gloriousgunpowder.models;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GunPartModel extends BakedModelWrapper<BakedModel> {

    public GunPartModel(BakedModel base) {
        super(base);
    }


}
