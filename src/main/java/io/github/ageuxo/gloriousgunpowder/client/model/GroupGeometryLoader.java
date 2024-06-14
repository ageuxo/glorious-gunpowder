package io.github.ageuxo.gloriousgunpowder.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupGeometryLoader implements IGeometryLoader<GroupGeometry> {
    public static final GroupGeometryLoader INSTANCE = new GroupGeometryLoader();
    public static final ResourceLocation ID = GloriousGunpowderMod.rl("group");

    private GroupGeometryLoader() {
    }

    @Override
    public @NotNull GroupGeometry read(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext deserializationContext) throws JsonParseException {
        List<BoneGroup> bones = List.of();
        if (jsonObject.has("groups")) {
            bones = BoneGroup.LIST_CODEC.parse(JsonOps.INSTANCE, jsonObject.get("groups")).getOrThrow();
        }
        jsonObject.remove("loader");
        BlockModel base = deserializationContext.deserialize(jsonObject, BlockModel.class);
        return new GroupGeometry(base, bones);
    }


}
