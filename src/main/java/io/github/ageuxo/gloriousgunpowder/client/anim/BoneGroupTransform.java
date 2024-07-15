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

    //TODO anim timer using gametime + partialTick


    public BoneGroupTransform setLerped(KeyTransform from, KeyTransform to, float t){

        from.position().lerp(to.position(), t, this.offset);
        from.scale().lerp(to.scale(), t, this.scale);
        from.rotation().slerp(to.rotation(), t, this.rotation);

        return this;
    }

    public BoneGroupTransform setFromKeyTransform(KeyTransform keyTransform){
        return set(keyTransform.position(), keyTransform.scale(), keyTransform.rotation());
    }

    public BoneGroupTransform set(Vector3f newOffset, Vector3f newScale, Quaternionf newRotation){
        this.offset.set(newOffset);
        this.scale.set(newScale);
        this.rotation.set(newRotation);
        return this;
    }

    public Vector3f offset() {
        return offset;
    }

    public void addOffset(Vector3f vec){
        this.offset.add(vec);
    }

    public Vector3f scale() {
        return scale;
    }

    public Quaternionf rotation() {
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
                "rotation=" + rotation;
    }

}
