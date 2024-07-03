package io.github.ageuxo.gloriousgunpowder.client.anim;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class Animation {
    private final ResourceLocation id;
    private final AnimationData data;

    public Animation(ResourceLocation id, AnimationData data) {
        this.id = id;
        this.data = data;
    }

    public ResourceLocation id() {
        return id;
    }

    public AnimationData data() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Animation anim) {
            return anim.id().equals(this.id());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Animation[" +
                "id=" + id + ']';
    }

}
