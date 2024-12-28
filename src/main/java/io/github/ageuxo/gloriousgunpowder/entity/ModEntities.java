package io.github.ageuxo.gloriousgunpowder.entity;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.BulletModel;
import io.github.ageuxo.gloriousgunpowder.client.render.BulletRenderer;
import io.github.ageuxo.gloriousgunpowder.entity.projectile.BulletProjectile;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class ModEntities {
    public static final ModelLayerLocation BULLET = new ModelLayerLocation(GloriousGunpowderMod.rl("bullet"), "main");
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, GloriousGunpowderMod.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletProjectile>> BULLET_PROJECTILE = register("bullet_projectile",
            EntityType.Builder.<BulletProjectile>of(BulletProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20).noSummon());

    protected static <T extends Entity> @NotNull DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name,
                () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, GloriousGunpowderMod.rl(name))));
    }

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
        bus.addListener(ModEntities::registerEntityLayers);
        bus.addListener(ModEntities::registerEntityRenderer);
    }

     static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BULLET_PROJECTILE.get(), BulletRenderer::new);
    }
    static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BULLET, BulletModel::createBodyLayer);
    }

}
