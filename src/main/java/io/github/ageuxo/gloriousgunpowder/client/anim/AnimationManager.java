package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AnimationManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final Codec<Map<String, GroupAnimationData>> ENTRY_CODEC = Codec.unboundedMap(Codec.STRING, GroupAnimationData.CODEC);

    public static AnimationManager INSTANCE = new AnimationManager(GSON);

    private Map<ResourceLocation, Animation> cache = ImmutableMap.of();

    private AnimationManager(Gson pGson) {
        super(pGson, "anim");
    }

    public Animation get(ResourceLocation location){
        return this.cache.get(location);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ImmutableMap.Builder<ResourceLocation, Animation> builder = ImmutableMap.builder();
        for (var entry : jsonMap.entrySet()){
            ResourceLocation key = entry.getKey();

            try {
                Map<String, GroupAnimationData> decoded = ENTRY_CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(JsonParseException::new);
                if (decoded.isEmpty()){
                    throw new JsonParseException("No entries defined");
                }
                builder.put(key, new Animation(key, decoded));
            } catch (JsonParseException e) {
                LOGGER.error("Parsing error loading animation {}", key, e);
            }
        }
        this.cache = builder.build();
        LOGGER.info("Loaded {} animations", this.cache.size());
    }

}
