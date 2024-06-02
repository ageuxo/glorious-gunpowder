package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import io.github.ageuxo.gloriousgunpowder.datagen.PartShapeTagProvider;
import io.github.ageuxo.gloriousgunpowder.item.GeoFirearm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.HashMap;
import java.util.Map;

public class GunRenderer extends GeoItemRenderer<GeoFirearm> {
    private static final Map<ResourceLocation, Pair<GeoModel<GeoFirearm>, TagKey<PartShape>>> SHAPE_MODEL_CACHE = new HashMap<>();
    private final Map<TagKey<PartShape>, GeoModel<GeoFirearm>> layer2ModelMap = new HashMap<>();

    public GunRenderer() {
        super(new DefaultedItemGeoModel<>(GloriousGunpowderMod.rl("gun_bones")));
        addRenderLayer(new GunComponentRenderLayer(this, PartShapeTagProvider.LOCKWORKS));
        addRenderLayer(new GunComponentRenderLayer(this, PartShapeTagProvider.BARRELS));
        addRenderLayer(new GunComponentRenderLayer(this, PartShapeTagProvider.STOCKS));
    }

    @Override
    public void preApplyRenderLayers(PoseStack poseStack, GeoFirearm animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        layer2ModelMap.clear();
        var components = this.currentItemStack.getComponents().get(GunDataComponents.GUN_COMPONENTS.get());
        if (components != null) {
            for (GunComponents component : components){
                Pair<GeoModel<GeoFirearm>, TagKey<PartShape>> modelPair = SHAPE_MODEL_CACHE.computeIfAbsent(component.shape(), GunRenderer::modelCachePair);
                layer2ModelMap.put(modelPair.getSecond(), modelPair.getFirst());
            }
        }
        super.preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager pResourceManager) {
        SHAPE_MODEL_CACHE.clear();
    }

    public GeoModel<GeoFirearm> getModelForLayer(TagKey<PartShape> tagKey){
        return layer2ModelMap.get(tagKey);
    }

    public static Pair<GeoModel<GeoFirearm>, TagKey<PartShape>> modelCachePair(ResourceLocation location){

        return Pair.of(new GunPartGeoModel<>(location), findLayer(location));
    }

    private static TagKey<PartShape> findLayer(ResourceLocation location){
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null){
            RegistryAccess registryAccess = clientLevel.registryAccess();
            PartShape shape = registryAccess.registryOrThrow(PartShape.KEY).get(location);
            if (shape != null) {
                if (shape.is(registryAccess, PartShapeTagProvider.BARRELS)) {
                    return PartShapeTagProvider.BARRELS;
                } else if (shape.is(registryAccess, PartShapeTagProvider.STOCKS)) {
                    return PartShapeTagProvider.STOCKS;
                } else if (shape.is(registryAccess, PartShapeTagProvider.LOCKWORKS)) {
                    return PartShapeTagProvider.LOCKWORKS;
                }
            }
        }
        return null;
    }
}
