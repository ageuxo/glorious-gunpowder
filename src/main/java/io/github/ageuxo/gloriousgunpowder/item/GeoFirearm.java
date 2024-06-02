package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.client.render.GunRenderer;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.datagen.MaterialProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.PartShapeProvider;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class GeoFirearm extends BaseFirearm implements GeoItem {
    public final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public GeoFirearm(Item.Properties pProperties) {
        super(pProperties.component(GunDataComponents.GUN_COMPONENTS.get(),
                List.of(
                        new GunComponents(MaterialProvider.WOOD.location(), PartShapeProvider.STANDARD_STOCK.location()),
                        new GunComponents(MaterialProvider.IRON.location(), PartShapeProvider.MATCHLOCK.location()),
                        new GunComponents(MaterialProvider.IRON.location(), PartShapeProvider.UNRIFLED_BARREL.location())
                )));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GunRenderer gunRenderer;
            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.gunRenderer == null){
                    this.gunRenderer = new GunRenderer();
                }
                return this.gunRenderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "lockworks", state -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }
}
