package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.render.GunRenderer;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.datagen.MaterialProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.PartShapeProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.PartShapeTagProvider;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GeoFirearm extends BaseFirearm implements GeoItem {
    private static final Map<String, ResourceLocation> TEST_MODEL_LOOKUP = createTestModelLookup();

    @NotNull
    private static HashMap<String, ResourceLocation> createTestModelLookup() {
        HashMap<String, ResourceLocation> map = new HashMap<>();
        map.put("root", GloriousGunpowderMod.rl("root"));
        map.put(PartShapeTagProvider.STOCKS.location().getPath(), PartShapeProvider.STANDARD_STOCK.location());
        map.put(PartShapeTagProvider.LOCKWORKS.location().getPath(), PartShapeProvider.MATCHLOCK.location());
        map.put(PartShapeTagProvider.BARRELS.location().getPath(), PartShapeProvider.UNRIFLED_BARREL.location());
        return map;
    }

    public final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public GeoFirearm(Item.Properties pProperties) {
        super(pProperties
                .component(GunDataComponents.GUN_COMPONENTS,
                    List.of(
                            new GunComponents(MaterialProvider.WOOD.location(), PartShapeProvider.STANDARD_STOCK.location()),
                            new GunComponents(MaterialProvider.IRON.location(), PartShapeProvider.MATCHLOCK.location()),
                            new GunComponents(MaterialProvider.IRON.location(), PartShapeProvider.UNRIFLED_BARREL.location())
                    )
                )
                .component(GunDataComponents.MODEL_LOOKUP, TEST_MODEL_LOOKUP)
        );
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
