package io.github.ageuxo.gloriousgunpowder.geo;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.item.FourPartGun;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class FourPartGunModel extends DefaultedItemGeoModel<FourPartGun> {
    public FourPartGunModel() {
        super(GloriousGunpowderMod.rl("four_part_gun"));
    }
}
