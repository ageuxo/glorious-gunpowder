package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.geo.GunRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class FourPartGun extends BaseFirearm implements GeoItem {
    private static final RawAnimation PRIME_ANIM = RawAnimation.begin().thenPlay("prime");
    private static final RawAnimation FIRE_ANIM = RawAnimation.begin().thenPlay("fire");

    private final AnimatableInstanceCache INSTANCE_CACHE = GeckoLibUtil.createInstanceCache(this);

    public FourPartGun(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>(this, "Main", 0, state -> PlayState.STOP)
                        .triggerableAnim("prime", PRIME_ANIM)
                        .triggerableAnim("fire", FIRE_ANIM)
        );

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return INSTANCE_CACHE;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GunRenderer renderer;
            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new GunRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void animateFiring(ServerLevel serverLevel, LivingEntity shooter, ItemStack weapon) {
        triggerAnim(shooter, GeoItem.getOrAssignId(weapon, serverLevel), "Main", "fire");
    }

    @Override
    public void animatePriming(ServerLevel serverLevel, LivingEntity shooter, ItemStack weapon) {
        triggerAnim(shooter, GeoItem.getOrAssignId(weapon, serverLevel), "Main", "prime");
    }
}
