package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<CreativeModeTab> GUN_CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GloriousGunpowderMod.MOD_ID);
    public static final ResourceKey<CreativeModeTab> GUN_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, GloriousGunpowderMod.rl("gun_tab"));
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GloriousGunpowderMod.MOD_ID);
    private static final Set<DeferredHolder<Item, ? extends Item>> GUN_TAB_ITEMS = new HashSet<>();
    public static final DeferredItem<BaseBulletItem> BULLET_ITEM = registerItem("bullet", () -> new BaseBulletItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<BasicFirearm> BASIC_FIREARM = registerItem("basic_firearm", () -> new BasicFirearm(new Item.Properties().durability(60)));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        GUN_CREATIVE_TAB.register(bus);
    }

    static <I extends Item> DeferredItem<I> registerItem(final String itemName, final Supplier<? extends I> itemSupplier) {
        DeferredItem<I> item = ITEMS.register(itemName, itemSupplier);
        GUN_TAB_ITEMS.add(item);
        return item;
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GUN_TAB = GUN_CREATIVE_TAB.register(GUN_TAB_KEY.location().getPath(), () -> CreativeModeTab.builder().displayItems(
            (pParameters, pOutput) -> GUN_TAB_ITEMS.forEach(item -> pOutput.accept(item.get())))
            .title(Component.translatable("itemGroup." + GloriousGunpowderMod.MOD_ID))
            .icon(() -> new ItemStack(BULLET_ITEM.get()))
            .build()
    );

}
