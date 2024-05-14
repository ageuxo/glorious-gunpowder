package io.github.ageuxo.gloriousgunpowder.data;

import io.github.ageuxo.gloriousgunpowder.GunRegistries;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class GunDataComponents {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunAttribute>> GUN_ATTRIBUTES = GunRegistries.COMPONENTS.register(
            "gun_attributes",
            GunRegistries.COMPONENTS.register("gun_attributes", () -> DataComponentType.<GunAttribute>builder().persistent(GunAttribute.CODEC).networkSynchronized(GunAttribute.STREAM_CODEC).build())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<GunComponents>>> GUN_COMPONENTS = GunRegistries.COMPONENTS.register(
            "gun_components",
            GunRegistries.COMPONENTS.register("gun_components", () -> DataComponentType.<List<GunComponents>>builder().persistent(GunComponents.LIST_CODEC).networkSynchronized(GunComponents.STREAM_CODEC.apply(ByteBufCodecs.list())).build())
    );
}
