package io.github.ageuxo.gloriousgunpowder.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;

public class GunEventFactory {
    public static void fireGunEvent(LivingEntity shooter, ItemStack gun) {
        GunFiredEvent event = new GunFiredEvent(shooter, gun);
        NeoForge.EVENT_BUS.post(event);
    }
}
