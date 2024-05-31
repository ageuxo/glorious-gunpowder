package io.github.ageuxo.gloriousgunpowder.entity;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.BulletModel;
import io.github.ageuxo.gloriousgunpowder.client.render.BulletRenderer;
import io.github.ageuxo.gloriousgunpowder.entity.projectile.BulletProjectile;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final ModelLayerLocation BULLET = new ModelLayerLocation(GloriousGunpowderMod.rl("bullet"), "main");
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, GloriousGunpowderMod.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletProjectile>> BULLET_PROJECTILE = ENTITY_TYPES.register("bullet_projectile", () -> EntityType.Builder.<BulletProjectile>of(BulletProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20).noSummon().build("bullet_projectile"));
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
