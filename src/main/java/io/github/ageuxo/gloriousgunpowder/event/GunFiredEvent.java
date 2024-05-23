package io.github.ageuxo.gloriousgunpowder.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public class GunFiredEvent extends Event {

    private final LivingEntity shooter;
    private ItemStack gun;
    public GunFiredEvent(LivingEntity shooter, ItemStack gun) {
        this.shooter = shooter;
        this.gun = gun;
    }
    public LivingEntity getShooter() {
        return shooter;
    }

    public ItemStack getGun() {
        return gun;
    }
}
