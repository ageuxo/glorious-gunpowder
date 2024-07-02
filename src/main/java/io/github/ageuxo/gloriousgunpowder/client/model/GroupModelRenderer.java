package io.github.ageuxo.gloriousgunpowder.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GroupModelRenderer {
    public GroupModelRenderer() {
    }

    public static void renderSubModel(PoseStack poseStack, VertexConsumer consumer, BakedModel model, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, RandomSource random, ModelData modelData, @Nullable RenderType renderType){

        PoseStack.Pose pose = poseStack.last();

        List<BakedQuad> quadList = model.getQuads(null, null, random, modelData, renderType);

        ModelBlockRenderer.renderQuadList(pose, consumer, red, green, blue, quadList, packedLight, packedOverlay);
    }

}
