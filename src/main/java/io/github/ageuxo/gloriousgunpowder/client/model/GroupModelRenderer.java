package io.github.ageuxo.gloriousgunpowder.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.util.RenderUtil;

import java.util.List;

public class GroupModelRenderer {
    private GroupModelRenderer() {
    }

    public static void renderSubModel(PoseStack poseStack, VertexConsumer consumer, BakedModel model, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, RandomSource random, ModelData modelData, @Nullable RenderType renderType){

        PoseStack.Pose pose = poseStack.last();

        List<BakedQuad> quadList = model.getQuads(null, null, random, modelData, renderType);

        ModelBlockRenderer.renderQuadList(pose, consumer, red, green, blue, quadList, packedLight, packedOverlay);
    }

    public static void rotateAroundPivot(PoseStack poseStack, GeoCube geoCube, Vector3f offset){
        translateToPivot(poseStack, geoCube, offset);
        RenderUtil.rotateMatrixAroundCube(poseStack, geoCube);
        translateFromPivot(poseStack, geoCube, offset);
    }

    private static void translateToPivot(PoseStack poseStack, GeoCube geoCube, Vector3f offset){
        Vec3 pivot = geoCube.pivot();
        poseStack.translate((pivot.x / 16) + offset.x, (pivot.y / 16) + offset.y, (pivot.z / 16) + offset.z);
    }

    private static void translateFromPivot(PoseStack poseStack, GeoCube geoCube, Vector3f offset){
        Vec3 pivot = geoCube.pivot();
        poseStack.translate(-(pivot.x / 16 + offset.x), -(pivot.y / 16 + offset.y), -(pivot.z / 16 + offset.z));
    }

    //This doesn't work
    public static void renderAxis(PoseStack poseStack, MultiBufferSource bufferSource, Vector3f origin, int lineLength){
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.debugLineStrip(4));
        drawLine(poseStack, buffer, origin, new Vector3f(origin).add(lineLength, 0, 0), ChatFormatting.RED);
        drawLine(poseStack, buffer, origin, new Vector3f(origin).add(0, 0, lineLength), ChatFormatting.GREEN);
        drawLine(poseStack, buffer, origin, new Vector3f(origin).add(0, lineLength, 0), ChatFormatting.BLUE);
    }

    private static void drawLine(PoseStack poseStack, VertexConsumer buffer, Vector3f start, Vector3f end, ChatFormatting colour) {
        var matrix = poseStack.last().pose();
        buffer.vertex(matrix, start.x, start.y, start.z).color(colour.getColor()&0xff);
        buffer.vertex(matrix, end.x, end.y, end.z).color(colour.getColor()&0xff);
    }
}
