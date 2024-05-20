package io.github.ageuxo.gloriousgunpowder.data;


import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class GunDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, GloriousGunpowderMod.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunAttribute>> GUN_ATTRIBUTES =
            COMPONENTS.register("gun_attributes", () -> DataComponentType.<GunAttribute>builder().persistent(GunAttribute.CODEC).networkSynchronized(GunAttribute.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<GunComponents>>> GUN_COMPONENTS =
            COMPONENTS.register("gun_components", () -> DataComponentType.<List<GunComponents>>builder().persistent(GunComponents.LIST_CODEC).networkSynchronized(GunComponents.STREAM_CODEC.apply(ByteBufCodecs.list())).build());
    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }

}
