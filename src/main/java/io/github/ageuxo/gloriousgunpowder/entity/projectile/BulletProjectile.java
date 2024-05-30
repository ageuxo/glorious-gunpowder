package io.github.ageuxo.gloriousgunpowder.entity.projectile;

import io.github.ageuxo.gloriousgunpowder.entity.ModEntities;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BulletProjectile extends Projectile {
    private static final EntityDataAccessor<Float> DATA_DAMAGE = SynchedEntityData.defineId(
            BulletProjectile.class, EntityDataSerializers.FLOAT
    );
    private float damage;
    public BulletProjectile(Level pLevel, LivingEntity shooter, float damage) {
        super(ModEntities.BULLET_PROJECTILE.get(), pLevel);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
        this.setOwner(shooter);
        this.damage = damage;
    }
    public BulletProjectile(Level pLevel, float damage, double pX, double pY, double pZ) {
        super(ModEntities.BULLET_PROJECTILE.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.damage = damage;
    }
    @Override
    public @NotNull Vec3 getMovementToShoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        return new Vec3(pX, pY, pZ)
                .normalize()
                .add(
                        random.triangle(0, pInaccuracy * random.nextFloat()),
                        random.triangle(0, pInaccuracy * random.nextFloat()),
                        random.triangle(0, pInaccuracy * random.nextFloat())
                )
                .scale(pVelocity);
    }



    public BulletProjectile(EntityType<? extends BulletProjectile> bulletProjectileEntityType, Level level) {
        super(bulletProjectileEntityType, level);
    }
    @Override
    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        boolean flag = false;
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
                flag = true;
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity blockentity = this.level().getBlockEntity(blockpos);
                if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
                }

                flag = true;
            }
        }

        if (hitresult.getType() != HitResult.Type.MISS && !flag && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            for (int i = 0; i < 4; i++) {
                this.level().addParticle(ParticleTypes.BUBBLE, d2 - vec3.x * 0.25, d0 - vec3.y * 0.25, d1 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale(f));
        this.applyGravity();
        this.setPos(d2, d0, d1);

    }
    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        super.onHitEntity(pResult);
        pResult.getEntity().hurt(this.damageSources().mobProjectile( this, (LivingEntity) this.getOwner()), damage);
        this.discard();
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_DAMAGE, 0f);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("damage", this.damage);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("damage")) {
            this.damage = pCompound.getFloat("damage");
        } else {
            this.damage = 0f;
        }
    }
    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }
}
