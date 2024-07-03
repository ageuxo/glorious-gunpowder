package io.github.ageuxo.gloriousgunpowder.client.anim;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public final class BoneGroupTransform {
    private final Vector3f offset;
    private final Vector3f scale;
    private final Quaternionf rotation;

    public BoneGroupTransform() {
        this(null, null, null);
    }

    public BoneGroupTransform(@Nullable Vector3f offset, @Nullable Vector3f scale, @Nullable Quaternionf rotation) {
        this.offset = offset != null ? offset : new Vector3f();
        this.scale = scale != null ? scale : new Vector3f(1);
        this.rotation = rotation != null ? rotation : new Quaternionf();
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

    public Quaternionf getRotation() {
        return rotation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BoneGroupTransform) obj;
        return Objects.equals(this.offset, that.offset) &&
                Objects.equals(this.scale, that.scale) &&
                Objects.equals(this.rotation, that.rotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, scale, rotation);
    }

    @Override
    public String toString() {
        return "BoneGroupTransform[" +
                "offset=" + offset + ", " +
                "scale=" + scale + ", " +
                "leftRot=" + rotation;
    }

}
