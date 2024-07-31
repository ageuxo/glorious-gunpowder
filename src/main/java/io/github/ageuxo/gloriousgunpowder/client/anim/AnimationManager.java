package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AnimationManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static AnimationManager INSTANCE = new AnimationManager(GSON);

    private Map<ResourceLocation, AnimationHolder> cache = ImmutableMap.of();

    private AnimationManager(Gson pGson) {
        super(pGson, "animations/dynamo");
    }

    public AnimationHolder get(ResourceLocation location){
        return this.cache.get(location);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        { //temp testing encode of codecs
            Map<String, GroupAnimationData> testMap = new HashMap<>();
            TreeMap<Integer, Vector3f> translationMap = new TreeMap<>();
            translationMap.put(2, new Vector3f(3));
            translationMap.put(3, new Vector3f(3));
            TreeMap<Integer, Vector3f> scaleMap = new TreeMap<>();
            scaleMap.put(19, new Vector3f(5));

            TreeMap<Integer, Quaternionf> rotationMap = new TreeMap<>();
            rotationMap.put(1, new Quaternionf(1, 2, 3, 4));
            testMap.put("test1", new GroupAnimationData(translationMap, scaleMap, rotationMap));
            try {
                UnboundedMapCodec<String, GroupAnimationData> mapCodec = Codec.unboundedMap(Codec.STRING, GroupAnimationData.CODEC);
                var test = mapCodec.encodeStart(JsonOps.INSTANCE, testMap);
                var testThrow = test.getOrThrow();
                var decode = mapCodec.parse(JsonOps.INSTANCE, testThrow);
            } catch (Exception e) {
                LOGGER.error("Error in test ", e);
            }
        }

        ImmutableMap.Builder<ResourceLocation, AnimationHolder> builder = ImmutableMap.builder();
        for (var entry : jsonMap.entrySet()){
            ResourceLocation key = entry.getKey();
            try {
                JsonObject holderJson = entry.getValue().getAsJsonObject();
                ImmutableMap.Builder<String, Animation> holderMap = new ImmutableMap.Builder<>();
                for (var anim : holderJson.getAsJsonObject("animations").entrySet()){
                    try {
                        JsonObject value = anim.getValue().getAsJsonObject();
                        DataResult<Map<String, GroupAnimationData>> parsed = Animation.DATA_CODEC.parse(JsonOps.INSTANCE, value.get("bones"));
                        Map<String, GroupAnimationData> bones = parsed.getOrThrow(JsonParseException::new);
                        holderMap.put(anim.getKey(), new Animation(anim.getKey(), value.get("animation_length").getAsFloat(), bones));
                    } catch (Exception e) {
                        LOGGER.warn("Skipping invalid animation in {}", key, e);
                    }
                }
                builder.put(key, new AnimationHolder(key, holderMap.build()));
            } catch (JsonParseException e) {
                LOGGER.error("Failed parsing animation file {}", key, e);
            }
        }
        this.cache = builder.build();
        LOGGER.info("Loaded {} animations", this.cache.size());
    }

}
