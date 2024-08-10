package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
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
            TreeMap<Float, Vector3f> translationMap = new TreeMap<>();
            translationMap.put(2f, new Vector3f(3));
            translationMap.put(3f, new Vector3f(3));
            TreeMap<Float, Vector3f> scaleMap = new TreeMap<>();
            scaleMap.put(19f, new Vector3f(5));

            TreeMap<Float, Quaternionf> rotationMap = new TreeMap<>();
            rotationMap.put(1f, new Quaternionf(1, 2, 3, 4));
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
            ResourceLocation holderKey = entry.getKey();
            ImmutableMap.Builder<String, Animation> mapBuilder = ImmutableMap.builder();
            for (var anim : entry.getValue().getAsJsonObject().getAsJsonObject("animations").entrySet()){
                String name = anim.getKey();
                try {
                    var result = Animation.CODEC.parse(JsonOps.INSTANCE, anim.getValue());
                    var decoded = result.getOrThrow();
                    mapBuilder.put(name, decoded);
                } catch (JsonParseException e) {
                    LOGGER.error("Failed loading animation {}", name, e);
                }
            }
            ImmutableMap<String, Animation> animMap = mapBuilder.build();
            AnimationHolder holder = new AnimationHolder(holderKey, animMap);
            builder.put(holderKey, holder);
        }
        this.cache = builder.build();
        LOGGER.info("Loaded {} animations", this.cache.size());
    }

}
