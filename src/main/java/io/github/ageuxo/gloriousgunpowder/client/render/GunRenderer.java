package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import io.github.ageuxo.gloriousgunpowder.client.model.GroupsModel;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.item.GeoFirearm;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.RenderUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class GunRenderer extends GeoItemRenderer<GeoFirearm> {
    private static final Long2ObjectOpenHashMap<BoneGeoModel<GeoFirearm>> INSTANCE_2_MODEL_MAP = new Long2ObjectOpenHashMap<>();
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
        BakedGeoModel model = getGeoModelForInstance().getBakedModel(getGeoModel().getModelResource(animatable));

        if (renderType == null)
            renderType = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);

        if (buffer == null && renderType != null)
            buffer = bufferSource.getBuffer(renderType);

        preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        actuallyRender(poseStack, animatable, model, renderType,
                bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.popPose();

    }

    @Override
    public void actuallyRender(PoseStack poseStack, GeoFirearm animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        BoneGeoModel<GeoFirearm> instanceGeoModel = getGeoModelForInstance();
        if (!isReRender) {
            AnimationState<GeoFirearm> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
            long instanceId = getInstanceId(animatable);

            animationState.setData(DataTickets.TICK, animatable.getTick(this.currentItemStack));
            animationState.setData(DataTickets.ITEM_RENDER_PERSPECTIVE, this.renderPerspective);
            animationState.setData(DataTickets.ITEMSTACK, this.currentItemStack);
            animatable.getAnimatableInstanceCache().getManagerForId(instanceId).setData(DataTickets.ITEM_RENDER_PERSPECTIVE, this.renderPerspective);
            instanceGeoModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
            instanceGeoModel.handleAnimations(animatable, instanceId, animationState);
        }

        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (buffer != null) {
            if (renderType == null){
                renderType = RenderType.cutout(); //Fallback RenderType
            }
//            updateAnimatedTextureFrame(animatable);

            @Nullable RenderType finalRenderType = renderType;
            GeoBone rootBone = model.topLevelBones().getFirst();
            for (GeoBone bone : rootBone.getChildBones()) {
                instanceGeoModel.getEitherModel(bone.getName())
                        .ifLeft( (bakedModel)-> renderModel(poseStack, bone, bakedModel, buffer, packedLight, packedOverlay, red, green, blue, alpha))
                        .ifRight( (groupsModel)-> renderGroupsModel(poseStack, animatable, bone, finalRenderType, bufferSource, buffer, isReRender, partialTick, packedLight,
                                packedOverlay, red, green, blue, alpha, instanceGeoModel, groupsModel));

            }
        }
    }

    public void renderGroupsModel(PoseStack poseStack, GeoFirearm animatable, GeoBone bone, RenderType finalRenderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, BoneGeoModel<GeoFirearm> instanceGeoModel, GroupsModel groupsModel) {
        for (BoneGroup boneGroup : groupsModel.getTopLevelGroups()){
            renderGroupRecursively(poseStack, animatable, bone, finalRenderType, bufferSource, buffer, isReRender, partialTick, packedLight,
                    packedOverlay, red, green, blue, alpha, instanceGeoModel, boneGroup);
        }
    }

    public BoneGeoModel<GeoFirearm> getGeoModelForInstance() {
        return INSTANCE_2_MODEL_MAP.computeIfAbsent(getInstanceId(this.animatable), id -> createGeoModelForInstance());
    }

    private BoneGeoModel<GeoFirearm> createGeoModelForInstance() {
        BoneGeoModel<GeoFirearm> boneGeoModel = new BoneGeoModel<>(GloriousGunpowderMod.rl("gun_bones"));
        Map<String, ResourceLocation> components = this.currentItemStack.getComponents().getOrDefault(GunDataComponents.MODEL_LOOKUP.get(), Map.of());
        boneGeoModel.populateGeoModel(components);
        return boneGeoModel;
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

    public void renderGroupRecursively(PoseStack poseStack, GeoFirearm animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, BoneGeoModel<GeoFirearm> boneGeoModel, BoneGroup boneGroup) {
        poseStack.pushPose();
        RenderUtil.prepMatrixForBone(poseStack, bone);
        renderGroupModel(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha, boneGeoModel, boneGroup);

        renderChildGroups(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha, boneGeoModel, boneGroup);
        poseStack.popPose();
    }

    public void renderGroupModel(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, BoneGeoModel<GeoFirearm> boneGeoModel, BoneGroup boneGroup) {
        if (bone.isHidden()){
            return;
        }

        BakedModel bakedModel = boneGroup.getBakedModel();
        if (bakedModel != null){
            poseStack.pushPose();

            RenderUtil.translateToPivotPoint(poseStack, bone);
            RenderUtil.rotateMatrixAroundBone(poseStack, bone);
            RenderUtil.translateAwayFromPivotPoint(poseStack, bone);


            this.modelRenderer.renderModel(poseStack.last(), buffer, null, bakedModel, red, green, blue, packedLight, packedOverlay, ModelData.EMPTY, RenderType.cutout());

            poseStack.popPose();
        }
    }

    public void renderChildGroups(PoseStack poseStack, GeoFirearm animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, BoneGeoModel<GeoFirearm> boneGeoModel, BoneGroup boneGroup) {

        for (BoneGroup group : boneGroup.getChildGroups()){
            renderGroupRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha, boneGeoModel, group);
        }
    }

    public void renderModel(PoseStack poseStack, GeoBone bone, BakedModel model, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isHidden()){
            return;
        }
        poseStack.pushPose();

        RenderUtil.translateToPivotPoint(poseStack, bone);
        RenderUtil.rotateMatrixAroundBone(poseStack, bone);
        RenderUtil.translateAwayFromPivotPoint(poseStack, bone);

        this.modelRenderer.renderModel(poseStack.last(), buffer, null, model, red, green, blue, packedLight, packedOverlay, ModelData.EMPTY, RenderType.cutout());
        poseStack.popPose();
    }

    @Override
    public @Nullable RenderType getRenderType(GeoFirearm animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.cutout();
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager pResourceManager) {
        INSTANCE_2_MODEL_MAP.clear();
        super.onResourceManagerReload(pResourceManager);
    }

}
