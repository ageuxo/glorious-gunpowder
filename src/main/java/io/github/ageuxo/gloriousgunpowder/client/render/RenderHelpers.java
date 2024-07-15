package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderHelpers {
    public static void translateToPivot(PoseStack poseStack, BoneGroup group){
        Vector3f pivot = group.origin();
        poseStack.translate(pivot.x, pivot.y, pivot.z);
    }

    public static void translateFromPivot(PoseStack poseStack, BoneGroup group){
        Vector3f pivot = group.origin();
        poseStack.translate(-pivot.x, -pivot.y, -pivot.z);
    }

    public static void rotateAroundPivot(PoseStack poseStack, Quaternionf rotation){
        poseStack.mulPose(rotation);
    }

    public static void scalePosestack(PoseStack poseStack, Vector3f scale){
        poseStack.scale(scale.x, scale.y, scale.z);
    }

    public static void translatePosestack(PoseStack poseStack, Vector3f offset){
        poseStack.translate(offset.x, offset.y, offset.z);
    }
}
