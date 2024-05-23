package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.GunRegistries;
import io.github.ageuxo.gloriousgunpowder.client.sound.ModSounds;
import io.github.ageuxo.gloriousgunpowder.data.GunAttribute;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStat;
import io.github.ageuxo.gloriousgunpowder.data.stats.GunStats;
import io.github.ageuxo.gloriousgunpowder.datagen.ItemTagProvider;
import io.github.ageuxo.gloriousgunpowder.entity.projectile.BulletProjectile;
import io.github.ageuxo.gloriousgunpowder.event.GunEventFactory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractFirearm extends ProjectileWeaponItem {
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    public AbstractFirearm(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack gun = pPlayer.getItemInHand(pHand);
        if (isReloaded(gun)) {
            float velocity = getGunAttributeValue(gun, GunStats.VELOCITY.get());
            this.shootBullet(pLevel, pPlayer, pHand, gun, velocity, 1.0F, null);
            return InteractionResultHolder.consume(gun);
        } else if (!pPlayer.getProjectile(gun).isEmpty()) {
            this.startSoundPlayed = false;
            this.midLoadSoundPlayed = false;
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(gun);
        } else {
            return InteractionResultHolder.fail(gun);
        }
    }
    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
        if (!pLevel.isClientSide) {
            SoundEvent initialSoundEvent = SoundEvents.NOTE_BLOCK_GUITAR.value();
            SoundEvent middleSoundEvent = SoundEvents.NOTE_BLOCK_BANJO.value();
            float f = (float)(pStack.getUseDuration() - pCount) / (float)(getUseDuration(pStack));
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                pLevel.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), initialSoundEvent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && middleSoundEvent != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                pLevel.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), middleSoundEvent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
    }
    public void shootBullet(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pWeapon, float pVelocity, float pInaccuracy, @Nullable LivingEntity pTarget) {
        if (!pLevel.isClientSide()) {
            ChargedProjectiles chargedprojectiles = pWeapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
                GunEventFactory.fireGunEvent(pShooter, pWeapon);
                this.shoot(pLevel, pShooter, pHand, pWeapon, chargedprojectiles.getItems(), pVelocity, pInaccuracy , pShooter instanceof Player, pTarget);
                if (pShooter instanceof ServerPlayer serverplayer) {
                    serverplayer.awardStat(Stats.ITEM_USED.get(pWeapon.getItem()));
                }

            }
        } else {
            pLevel.playSound(pShooter, pShooter.getOnPos(), ModSounds.GUN_SHOT_SOUND.get(), SoundSource.PLAYERS, 0.5F, 1.25F);
            GunEventFactory.fireGunEvent(pShooter, pWeapon);
        }
    }
    @Override
    protected Projectile createProjectile(Level pLevel, LivingEntity pShooter, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit) {;
        float damage = getGunAttributeValue(pWeapon, GunStats.DAMAGE.get());
        return new BulletProjectile(pLevel, pShooter, damage);
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        float reloadSpeed = getGunAttributeValue(pStack, GunStats.ACCURACY.get());
        return (int) (reloadSpeed * 20 + 3);
    }

    @Override
    protected void shootProjectile(
            LivingEntity pShooter, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @Nullable LivingEntity pTarget
    ) {
        ItemStack gun = pShooter.getItemInHand(InteractionHand.MAIN_HAND);
        float accuracy = getGunAttributeValue(gun, GunStats.ACCURACY.get()) * 10;
        Vector3f vector3f;
        if (pTarget != null && pProjectile instanceof BulletProjectile) {
            double d0 = pTarget.getX() - pShooter.getX();
            double d1 = pTarget.getZ() - pShooter.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = pTarget.getY(1 / 3f) - pProjectile.getY() + d2 * 0.2F;
            vector3f = getProjectileShotVector(pShooter, new Vec3(d0, d3, d1), pAngle);
        } else {
            Vec3 vec3 = pShooter.getUpVector(1.0F);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis((double)(pAngle * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = pShooter.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        pProjectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), pVelocity,  1 / accuracy);
    }

    private static Vector3f getProjectileShotVector(LivingEntity pShooter, Vec3 pDistance, float pAngle) {
        Vector3f vector3f = pDistance.toVector3f().normalize();
        Vector3f vector3f1 = new Vector3f(vector3f).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = pShooter.getUpVector(1.0F);
            vector3f1 = new Vector3f(vector3f).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = new Vector3f(vector3f).rotateAxis((float) (Math.PI / 2), vector3f1.x, vector3f1.y, vector3f1.z);
        return new Vector3f(vector3f).rotateAxis(pAngle * (float) (Math.PI / 180.0), vector3f2.x, vector3f2.y, vector3f2.z);
    }
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        float reloadDuration = this.getUseDuration(pStack) - pTimeLeft;
        boolean canReload = reloadDuration / getUseDuration(pStack) > 1;
        if (canReload && !isReloaded(pStack) && tryLoadBullet(pEntityLiving, pStack)) {
            pLevel.playSound(
                    null,
                    pEntityLiving.getX(),
                    pEntityLiving.getY(),
                    pEntityLiving.getZ(),
                    SoundEvents.NOTE_BLOCK_HARP.value(),
                    pEntityLiving.getSoundSource(),
                    1.0F,
                    1.0F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
            );
        }

    }

    private static boolean tryLoadBullet(LivingEntity pShooter, ItemStack gun) {
        List<ItemStack> list = draw(gun, pShooter.getProjectile(gun), pShooter);
        if (!list.isEmpty()) {
            gun.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            return true;
        } else {
            return false;
        }
    }
    public static boolean isReloaded(ItemStack gun) {
        ChargedProjectiles chargedprojectiles = gun.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        return !chargedprojectiles.isEmpty();
    }
    @Override
    public @NotNull Predicate<ItemStack> getSupportedHeldProjectiles() {
        return item -> item.is(ItemTagProvider.BULLET);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return item -> item.is(ItemTagProvider.BULLET);
    }
    @Override
    public int getDefaultProjectileRange() {
        return 16;
    }
    @Override
    public boolean useOnRelease(ItemStack pStack) {
        return pStack.is(this);
    }
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    public void setGunAttributes(ItemStack stack, GunAttribute... gunAttributes) {
        List<GunAttribute> existingGunAttributes = new ArrayList<>(List.of(gunAttributes));
        stack.set(GunDataComponents.GUN_ATTRIBUTES.get(), existingGunAttributes);
    }
    public GunAttribute getGunAttribute(ItemStack stack, GunStat stat) {
        String id = stat.name();
        List<GunAttribute> currentAttributes = stack.get(GunDataComponents.GUN_ATTRIBUTES);
        if (currentAttributes != null) {
            for (GunAttribute attribute : currentAttributes) {
                if (attribute.stat().equals(id)) {
                    return attribute;
                }
            }
        }
        return new GunAttribute(stat.name(), stat.defaultValue());
    }
    public float getGunAttributeValue(ItemStack stack, GunStat stat) {
        return (float) getGunAttribute(stack, stat).value();
    }
    public void setNewComponents(ItemStack stack, GunComponents... components) {
        List<GunComponents> existingComponentData = new ArrayList<>(stack.getOrDefault(GunDataComponents.GUN_COMPONENTS.get(), new ArrayList<>()));
        existingComponentData.addAll(List.of(components));
        stack.set(GunDataComponents.GUN_COMPONENTS.get(), existingComponentData);
    }



}
