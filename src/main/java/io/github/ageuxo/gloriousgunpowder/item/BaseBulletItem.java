package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.entity.projectile.BulletProjectile;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BaseBulletItem extends Item implements ProjectileItem {
    public BaseBulletItem(Properties pProperties) {
        super(pProperties);
    }
    //This should ideally never be used as it doesn't make sense for bullets to be used by anything that isnt a gun, but we provide a generic bullet damage
    @Override
    public @NotNull Projectile asProjectile(@NotNull Level pLevel, Position pPos, @NotNull ItemStack pStack, @NotNull Direction pDirection) {
        return new BulletProjectile(pLevel, 5f, pPos.x(), pPos.y(), pPos.z());
    }
    public BulletProjectile createBullet(Level pLevel, float damage, LivingEntity pShooter) {
        return new BulletProjectile(pLevel, pShooter, damage);
    }
}
