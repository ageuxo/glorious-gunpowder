package io.github.ageuxo.gloriousgunpowder.client.render;

import io.github.ageuxo.gloriousgunpowder.item.GeoFirearm;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GunComponentRenderer extends GeoItemRenderer<GeoFirearm> {
    public GunComponentRenderer(GeoModel<GeoFirearm> model) {
        super(model);
    }
}
