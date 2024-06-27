package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BoneGroupVertexConsumer extends VertexConsumerWrapper {
    private Transformation transformation;

    public BoneGroupVertexConsumer(VertexConsumer parent) {
        super(parent);
    }

    public void setFromBone(BoneGroup bone){
        this.transformation = bone.buildTransform();
    }

    @Override
    public @NotNull VertexConsumer vertex(double x, double y, double z) {
        Vector4f vec = new Vector4f((float) x, (float) y, (float) z, 1);
        transformation.transformPosition(vec);
        vec.div(vec.w);
        return super.vertex(vec.x, vec.y, vec.z);
    }

    @Override
    public @NotNull VertexConsumer normal(float x, float y, float z) {
        Vector3f vec = new Vector3f(x, y, z);
        transformation.transformNormal(vec);
        vec.normalize();
        return super.normal(vec.x, vec.y, vec.z);
    }
}
