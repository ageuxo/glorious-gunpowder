package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.data.GunAttribute;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFirearm extends Item {
    public AbstractFirearm(Properties pProperties) {
        super(pProperties);
    }
    public void setGunAttributes(ItemStack stack, GunAttribute gunAttribute) {
        stack.set(GunDataComponents.GUN_ATTRIBUTES, gunAttribute);
    }
    public GunAttribute getGunAttributes(ItemStack stack) {
        return stack.getOrDefault(GunDataComponents.GUN_ATTRIBUTES, new GunAttribute(0, 1, 1f));
    }
    public void setNewComponents(ItemStack stack, List<GunComponents> components) {
        List<GunComponents> existingComponentData = new ArrayList<>(stack.getOrDefault(GunDataComponents.GUN_COMPONENTS, new ArrayList<>()));
        existingComponentData.addAll(components);
        stack.set(GunDataComponents.GUN_COMPONENTS, existingComponentData);
    }
}
