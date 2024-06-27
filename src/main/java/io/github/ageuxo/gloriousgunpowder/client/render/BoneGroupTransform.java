package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.math.Transformation;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public final class BoneGroupTransform {
    private final Vector3f offset;
    private final Vector3f scale;
    private final Quaternionf leftRot;
    private final Quaternionf rightRot;

    public BoneGroupTransform() {
        this(null, null, null, null);
    }

    public BoneGroupTransform(@Nullable Vector3f offset, @Nullable Vector3f scale, @Nullable Quaternionf leftRot, @Nullable Quaternionf rightRot) {
        this.offset = offset != null ? offset : new Vector3f();
        this.scale = scale != null ? scale : new Vector3f(1);
        this.leftRot = leftRot != null ? leftRot : new Quaternionf();
        this.rightRot = rightRot != null ? rightRot : new Quaternionf();
    }

    public Transformation build(BoneGroup boneGroup) {
        return new Transformation(boneGroup.origin().div(16).add(this.offset), this.leftRot, this.scale, this.rightRot);
    }

    public Vector3f getOffset() {
        return offset;
    }

    public void addOffset(Vector3f vec){
        this.offset.add(vec);
    }

    public Vector3f getScale() {
        return scale;
    }

    public Quaternionf getLeftRot() {
        return leftRot;
    }

    public Quaternionf getRightRot() {
        return rightRot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BoneGroupTransform) obj;
        return Objects.equals(this.offset, that.offset) &&
                Objects.equals(this.scale, that.scale) &&
                Objects.equals(this.leftRot, that.leftRot) &&
                Objects.equals(this.rightRot, that.rightRot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, scale, leftRot, rightRot);
    }

    @Override
    public String toString() {
        return "BoneGroupTransform[" +
                "offset=" + offset + ", " +
                "scale=" + scale + ", " +
                "leftRot=" + leftRot + ", " +
                "rightRot=" + rightRot + ']';
    }

}
