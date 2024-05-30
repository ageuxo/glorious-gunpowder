package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.client.render.GunRenderer;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class GeoFirearm extends AbstractFirearm implements GeoItem {
    public final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public GeoFirearm(Properties pProperties) {
        super(pProperties.component(GunDataComponents.GUN_COMPONENTS.get(), List.of()));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GunRenderer renderer = null;
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null){
                    this.renderer = new GunRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }
}
