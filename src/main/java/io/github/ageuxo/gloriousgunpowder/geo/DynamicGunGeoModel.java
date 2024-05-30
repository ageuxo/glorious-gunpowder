package io.github.ageuxo.gloriousgunpowder.geo;

import io.github.ageuxo.gloriousgunpowder.item.GeoFirearm;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DynamicGunGeoModel extends GeoModel<GeoFirearm> {
    @Override
    public ResourceLocation getModelResource(GeoFirearm animatable) {
        return null;
    }

    @Override
    public ResourceLocation getTextureResource(GeoFirearm animatable) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationResource(GeoFirearm animatable) {
        return null;
    }
}
