package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import io.github.ageuxo.gloriousgunpowder.item.GeoFirearm;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

import java.util.Optional;

public class GunComponentRenderLayer extends GeoRenderLayer<GeoFirearm> {
    public final String bone;
    public final TagKey<PartShape> tagKey;
    public GeoModel<GeoFirearm> layerModel;
    public GunRenderer gunRenderer;

    public GunComponentRenderLayer(GunRenderer entityRendererIn, TagKey<PartShape> tagKey) {
        super(entityRendererIn);
        this.gunRenderer = entityRendererIn;
        this.tagKey = tagKey;
        this.bone = tagKey.location().getPath();
    }

    @Override
    public void preRender(PoseStack poseStack, GeoFirearm animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        this.layerModel = this.gunRenderer.getModelForLayer(this.tagKey);
    }

    @Override
    public void render(PoseStack poseStack, GeoFirearm animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (layerModel != null){
            BakedGeoModel bakedLayerModel = this.layerModel.getBakedModel(this.layerModel.getModelResource(this.renderer.getAnimatable()));
            GeoBone rootBone = bakedModel.topLevelBones().getFirst();
            if (rootBone != null){
                GeoBone geoBone = bakedModel.searchForChildBone(rootBone, this.bone);
                poseStack.pushPose();
                Color renderColor = this.gunRenderer.getRenderColor(animatable, partialTick, packedLight);
                float red = renderColor.getRedFloat();
                float green = renderColor.getGreenFloat();
                float blue = renderColor.getBlueFloat();
                float alpha = renderColor.getAlphaFloat();

                if (geoBone != null){
                    Vector3d vector3d = geoBone.getModelPosition();
                    poseStack.translate(vector3d.x, vector3d.y, vector3d.z);
                    if (buffer != null) {
                        for (GeoBone modelBone : bakedLayerModel.topLevelBones()) {
                            this.gunRenderer.renderRecursively(poseStack, animatable, modelBone, renderType, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
                        }
                    }
                }
            }
            poseStack.popPose();
        }
    }
}
