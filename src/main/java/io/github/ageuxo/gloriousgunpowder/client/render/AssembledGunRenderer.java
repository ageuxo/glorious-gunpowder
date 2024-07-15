package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import io.github.ageuxo.gloriousgunpowder.client.anim.AnimatableInstance;
import io.github.ageuxo.gloriousgunpowder.client.anim.BoneGroupTransform;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import io.github.ageuxo.gloriousgunpowder.client.model.GroupModelRenderer;
import io.github.ageuxo.gloriousgunpowder.client.model.GroupsModel;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AssembledGunRenderer extends BlockEntityWithoutLevelRenderer {
    private static final Long2ObjectMap<AnimatableInstance> ID_INSTANCE_MAP = new Long2ObjectOpenHashMap<>();
    private static final List<Pair<String, Vector3f>> ANCHORS = List.of(
            Pair.of("lockworks", new Vector3f()),
            Pair.of("barrels", new Vector3f()),
            Pair.of("stocks", new Vector3f())
    );

    public final BoneGroupTransform transform = new BoneGroupTransform();
    protected ItemStack stack;

    public AssembledGunRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    private AnimatableInstance getInstance(long id){
        return ID_INSTANCE_MAP.computeIfAbsent(id, this::createAnimatableInstance);
    }

    private AnimatableInstance createAnimatableInstance(long id) {
        AnimatableInstance instance = new AnimatableInstance(id);
        Map<String, ResourceLocation> gunComponents = this.stack.getComponents().getOrDefault(GunDataComponents.MODEL_LOOKUP.get(), Map.of());
        instance.populateInstance(gunComponents);
        return instance;
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        this.stack = pStack;
        RenderType renderType = RenderType.cutout();
        VertexConsumer buffer = pBuffer.getBuffer(renderType);
        Long instanceId = this.stack.getComponents().get(GunDataComponents.ANIM_INSTANCE_ID.get());
        if (instanceId != null){
            render(pPoseStack, pBuffer, buffer, Minecraft.getInstance().getFrameTime(), pPackedLight, pPackedOverlay, instanceId, renderType);
        }
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, long instanceId, RenderType renderType){
        AnimatableInstance instance = getInstance(instanceId);

        for (var anchor : ANCHORS){
            Either<BakedModel, GroupsModel> either = instance.getEitherModel(anchor.getFirst());
            if (either != null){
                poseStack.pushPose();
                Vector3f offset = anchor.getSecond();
                poseStack.translate(offset.x(), offset.y(), offset.z());
                RandomSource rand = RandomSource.create();
                rand.setSeed(42L);
                either.ifLeft(bakedModel-> renderBakedModel(poseStack, buffer, bakedModel, packedLight, packedOverlay, renderType, rand))
                        .ifRight(groupsModel -> renderGroupsModel(poseStack, buffer, instance, groupsModel, partialTick, packedLight, packedOverlay, renderType, rand));
                poseStack.popPose();
            }
        }
    }

    public void renderBakedModel(PoseStack poseStack, VertexConsumer buffer, BakedModel model, int packedLight, int packedOverlay, RenderType renderType, RandomSource rand){
        GroupModelRenderer.renderModel(poseStack, buffer, model, 1, 1, 1, 1, packedLight, packedOverlay, rand, ModelData.EMPTY, renderType);
    }

    public void renderGroupsModel(PoseStack poseStack, VertexConsumer buffer, AnimatableInstance instance, GroupsModel groupsModel, float partialTick, int packedLight, int packedOverlay, RenderType renderType, RandomSource rand){
        for (BoneGroup group : groupsModel.getTopLevelGroups()){
            renderGroupRecursively(poseStack, buffer, instance, group, partialTick, packedLight, packedOverlay, 1f, 1f, 1f, 1f, renderType, rand);
        }
    }

    public void renderGroupRecursively(PoseStack poseStack, VertexConsumer buffer, AnimatableInstance instance, BoneGroup boneGroup, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, RenderType renderType, RandomSource rand) {
        poseStack.pushPose();

        setupPoseForGroup(poseStack, instance, boneGroup, partialTick);
        GroupModelRenderer.renderModel(poseStack, buffer, boneGroup.getBakedModel(), red, green, 1f, 1f, packedLight, packedOverlay, rand, ModelData.EMPTY, renderType);

        renderSubGroups(poseStack, buffer, instance, boneGroup, partialTick, packedLight, packedOverlay, red, green, blue, alpha, renderType, rand);

        poseStack.popPose();
    }

    public void renderSubGroups(PoseStack poseStack, VertexConsumer buffer, AnimatableInstance instance, BoneGroup group, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, RenderType renderType, RandomSource rand){
        for (BoneGroup child : group.getChildGroups()){
            renderGroupRecursively(poseStack, buffer, instance, child, partialTick, packedLight, packedOverlay, red, green, blue, alpha, renderType, rand);
        }
    }

    public void setupPoseForGroup(PoseStack poseStack, AnimatableInstance instance, BoneGroup group, float partialTick){
        BoneGroupTransform transforms = instance.setGroupTransformForTick(this.transform, group, getGameTime(), partialTick);

        RenderHelpers.translatePosestack(poseStack, transforms.offset());
        RenderHelpers.translateToPivot(poseStack, group);
        RenderHelpers.rotateAroundPivot(poseStack, transforms.rotation());
        RenderHelpers.scalePosestack(poseStack, transforms.scale());
        RenderHelpers.translateFromPivot(poseStack, group);
    }

    private long getGameTime(){
        ClientLevel level = Minecraft.getInstance().level;
        return level != null ? level.getGameTime() : 0;
    }




}
