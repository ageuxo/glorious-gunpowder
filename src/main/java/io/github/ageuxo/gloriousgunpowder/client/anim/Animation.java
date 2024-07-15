package io.github.ageuxo.gloriousgunpowder.client.anim;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Objects;

public final class Animation {
    public static final Animation EMPTY = new Animation(GloriousGunpowderMod.rl("empty"), Map.of());
    private final ResourceLocation id;
    private final Map<String, GroupAnimationData> data;

    public Animation(ResourceLocation id, Map<String, GroupAnimationData> data) {
        this.id = id;
        this.data = data;
    }

    public ResourceLocation id() {
        return id;
    }

    public Map<String, GroupAnimationData> data() {
        return data;
    }

    public GroupAnimationData getGroupData(BoneGroup group){
        return data.get(group.name());
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
