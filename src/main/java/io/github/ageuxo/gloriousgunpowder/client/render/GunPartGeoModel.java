package io.github.ageuxo.gloriousgunpowder.client.render;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class GunPartGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {
    /**
     * Create a new instance of this model class
     * <p>
     * The asset path should be the truncated relative path from the base folder
     * <p>
     * E.G.
     * <pre>
     *     {@code
     * 		new ResourceLocation("myMod", "animals/red_fish")
     *                }</pre>
     *
     * @param assetSubpath
     */
    public GunPartGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    protected String subtype() {
        return "gun_part";
    }
}
