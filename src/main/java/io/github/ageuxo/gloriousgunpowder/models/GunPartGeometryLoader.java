package io.github.ageuxo.gloriousgunpowder.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GunPartGeometryLoader implements IGeometryLoader<GunPartGeometry> {

    public static final GunPartGeometryLoader INSTANCE = new GunPartGeometryLoader();
    public static final ResourceLocation ID = GloriousGunpowderMod.rl("gun_part");

    private GunPartGeometryLoader() {
    }

    @Override
    public GunPartGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        jsonObject.remove("loader");
        BlockModel base = deserializationContext.deserialize(jsonObject, BlockModel.class);
        return new GunPartGeometry(base);
    }
}
