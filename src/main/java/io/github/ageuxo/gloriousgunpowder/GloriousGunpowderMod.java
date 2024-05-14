package io.github.ageuxo.gloriousgunpowder;

import io.github.ageuxo.gloriousgunpowder.block.ModBlocks;
import io.github.ageuxo.gloriousgunpowder.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(GloriousGunpowderMod.MOD_ID)
public class GloriousGunpowderMod {
    public static final String MOD_ID = "glorious_gunpowder";

    public GloriousGunpowderMod(IEventBus eventBus) {
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
    }

}
