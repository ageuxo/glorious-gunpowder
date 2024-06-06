package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.item.GeoFirearm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.Color;

public class GunRenderer extends GeoItemRenderer<GeoFirearm> {
    private final ModelBlockRenderer modelRenderer;


    public GunRenderer() {
        super(new BoneGeoModel<>(GloriousGunpowderMod.rl("gun_bones")));
        this.modelRenderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
    }

    @Override
    public void defaultRender(PoseStack poseStack, GeoFirearm animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        poseStack.pushPose();

        Color renderColor = getRenderColor(animatable, partialTick, packedLight);
        float red = renderColor.getRedFloat();
        float green = renderColor.getGreenFloat();
        float blue = renderColor.getBlueFloat();
        float alpha = renderColor.getAlphaFloat();
        int packedOverlay = getPackedOverlay(animatable, 0, partialTick);
        BakedGeoModel model = getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable));

        if (renderType == null)
            renderType = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);

        if (buffer == null && renderType != null)
            buffer = bufferSource.getBuffer(renderType);

        preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        if (firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight)) {
            preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, packedLight, packedLight, packedOverlay);
            actuallyRender(poseStack, animatable, model, renderType,
                    bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            postRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
        }

        poseStack.popPose();

        renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, GeoFirearm animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender) {
            AnimationState<GeoFirearm> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
            long instanceId = getInstanceId(animatable);
            GeoModel<GeoFirearm> currentModel = getGeoModel();

            animationState.setData(DataTickets.TICK, animatable.getTick(this.currentItemStack));
            animationState.setData(DataTickets.ITEM_RENDER_PERSPECTIVE, this.renderPerspective);
            animationState.setData(DataTickets.ITEMSTACK, this.currentItemStack);
            animatable.getAnimatableInstanceCache().getManagerForId(instanceId).setData(DataTickets.ITEM_RENDER_PERSPECTIVE, this.renderPerspective);
            currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
            currentModel.handleAnimations(animatable, instanceId, animationState);
        }

        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (buffer != null) {
//            updateAnimatedTextureFrame(animatable);

            for (GeoBone group : model.topLevelBones()) {
                renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight,
                        packedOverlay, red, green, blue, alpha);
            }
        }
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        this.animatable = (GeoFirearm) stack.getItem();
        this.currentItemStack = stack;
        this.renderPerspective = transformType;

        RenderType renderType = RenderType.cutout();
        if (transformType == ItemDisplayContext.GUI) {
            renderInGui(transformType, poseStack, bufferSource, packedLight, packedOverlay, renderType);
        }
        else {
            VertexConsumer buffer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, false, this.currentItemStack != null && this.currentItemStack.hasFoil());

            defaultRender(poseStack, this.animatable, bufferSource, renderType, buffer,
                    0, Minecraft.getInstance().getFrameTime(), packedLight);
        }
    }

    protected void renderInGui(ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, RenderType renderType) {
        MultiBufferSource.BufferSource defaultBufferSource = bufferSource instanceof MultiBufferSource.BufferSource bufferSource2 ?
                bufferSource2 : Minecraft.getInstance().levelRenderer.renderBuffers.bufferSource();
        VertexConsumer buffer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, true, this.currentItemStack != null && this.currentItemStack.hasFoil());

        poseStack.pushPose();

        if (this.useEntityGuiLighting) {
            Lighting.setupForEntityInInventory();
        }
        else {
            Lighting.setupForFlatItems();
        }

        defaultRender(poseStack, this.animatable, defaultBufferSource, renderType, buffer,
                0, Minecraft.getInstance().getFrameTime(), packedLight);
        defaultBufferSource.endBatch();
        RenderSystem.enableDepthTest();
        Lighting.setupFor3DItems();
        poseStack.popPose();
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isHidden()){
            return;
        }

        BoneGeoModel<GeoFirearm> boneGeoModel = (BoneGeoModel<GeoFirearm>) getGeoModel();
        BakedModel bakedModel = boneGeoModel.get(bone);
        if (bakedModel != null){
            this.modelRenderer.renderModel(poseStack.last(), buffer, null, bakedModel, red, green, blue, packedLight, packedOverlay);
        } else {
            throw new RuntimeException("Attempted to render null model for bone: "+ bone + " in model: "+ boneGeoModel);
        }

    }

    @Override
    public @Nullable RenderType getRenderType(GeoFirearm animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.cutout();
    }
}
