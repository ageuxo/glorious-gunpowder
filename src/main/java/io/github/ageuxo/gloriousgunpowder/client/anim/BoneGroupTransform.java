package io.github.ageuxo.gloriousgunpowder.client.anim;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public final class BoneGroupTransform {
    private final Vector3f translation;
    private final Vector3f scale;
    private final Quaternionf rotation;

    public BoneGroupTransform() {
        this(null, null, null);
    }

    public BoneGroupTransform(@Nullable Vector3f translation, @Nullable Vector3f scale, @Nullable Quaternionf rotation) {
        this.translation = translation != null ? translation : new Vector3f();
        this.scale = scale != null ? scale : new Vector3f(1);
        this.rotation = rotation != null ? rotation : new Quaternionf();
    }

    //TODO anim timer using gametime + partialTick


    public BoneGroupTransform setLerped(GroupAnimationData anim, int tick, float t){
        return setLerped(anim.getTranslation(tick), anim.getScale(tick), anim.getRotation(tick), anim.getNextTranslation(tick), anim.getNextScale(tick), anim.getNextRotation(tick), t);
    }

    public BoneGroupTransform setLerped(Vector3f fromTranslation, Vector3f fromScale, Quaternionf fromRotation, Vector3f toTranslation, Vector3f toScale, Quaternionf toRotation, float t){
        fromTranslation.lerp(toTranslation, t, this.translation);
        fromScale.lerp(toScale, t, this.scale);
        fromRotation.slerp(toRotation, t, this.rotation);

        return this;
    }

    public BoneGroupTransform set(Vector3f newOffset, Vector3f newScale, Quaternionf newRotation){
        this.translation.set(newOffset);
        this.scale.set(newScale);
        this.rotation.set(newRotation);
        return this;
    }

    public Vector3f translation() {
        return translation;
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
        return Objects.equals(this.translation, that.translation) &&
                Objects.equals(this.scale, that.scale) &&
                Objects.equals(this.rotation, that.rotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translation, scale, rotation);
    }

    @Override
    public String toString() {
        return "BoneGroupTransform[" +
                "offset=" + translation + ", " +
                "scale=" + scale + ", " +
                "rotation=" + rotation;
    }

}
