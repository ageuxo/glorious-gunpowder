package io.github.ageuxo.gloriousgunpowder.block;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(GloriousGunpowderMod.MOD_ID);

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
