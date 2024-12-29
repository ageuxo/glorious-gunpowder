package io.github.ageuxo.gloriousgunpowder.geo;

import io.github.ageuxo.gloriousgunpowder.item.FourPartGun;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GunRenderer extends GeoItemRenderer<FourPartGun> {

    public GunRenderer() {
        super(new FourPartGunModel());
    }


}
