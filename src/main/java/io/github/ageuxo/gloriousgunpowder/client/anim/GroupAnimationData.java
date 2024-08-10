package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;
import java.util.TreeMap;

public class GroupAnimationData {
    public static final Codec<Float> FLOAT_STRING_KEY = Codec.STRING.comapFlatMap(
            s -> {
                try {
                    return DataResult.success(Float.valueOf(s));
                } catch (NumberFormatException e) {
                    return DataResult.error(()->s + " is not a float");
                }
            },
            aFloat -> Float.toString(aFloat));
    public static final Codec<TreeMap<Float, Vector3f>> VEC3_ENTRY_CODEC = Codec.unboundedMap(FLOAT_STRING_KEY, ExtraCodecs.VECTOR3F).xmap(TreeMap::new, Map::copyOf);
    public static final Codec<TreeMap<Float, Quaternionf>> QUAT_ENTRY_CODEC = Codec.unboundedMap(FLOAT_STRING_KEY, ExtraCodecs.VECTOR3F.xmap(vec -> new Quaternionf(vec.x(), vec.y(), vec.z(), 0), quat -> new Vector3f(quat.x(), quat.y(), quat.z()))).xmap(TreeMap::new, Map::copyOf);
    public static final Codec<GroupAnimationData> CODEC = RecordCodecBuilder.create(instance->instance.group(
        VEC3_ENTRY_CODEC.optionalFieldOf("position", new TreeMap<>()).forGetter(GroupAnimationData::translationMap),
        VEC3_ENTRY_CODEC.optionalFieldOf("scale", new TreeMap<>()).forGetter(GroupAnimationData::scaleMap),
        QUAT_ENTRY_CODEC.optionalFieldOf("rotation", new TreeMap<>()).forGetter(GroupAnimationData::rotationMap)
    ).apply(instance, GroupAnimationData::new));
    private final TreeMap<Float, Vector3f> translationMap;
    private final TreeMap<Float, Vector3f> scaleMap;
    private final TreeMap<Float, Quaternionf> rotationMap;

    public GroupAnimationData(TreeMap<Float, Vector3f> translationMap, TreeMap<Float, Vector3f> scaleMap, TreeMap<Float, Quaternionf> rotationMap) {
        this.translationMap = translationMap;
        this.scaleMap = scaleMap;
        this.rotationMap = rotationMap;
    }

    public Vector3f getTranslation(float frame){
        return translationMap.floorEntry(frame).getValue();
    }

    public Vector3f getNextTranslation(float frame){
        Map.Entry<Float, Vector3f> nextEntry = translationMap.higherEntry(frame);
        if (nextEntry == null){
            return translationMap.lastEntry().getValue();
        } else {
            return nextEntry.getValue();
        }
    }

    public Vector3f getScale(float frame){
        return scaleMap.floorEntry(frame).getValue();
    }

    public Vector3f getNextScale(float frame){
        Map.Entry<Float, Vector3f> nextEntry = scaleMap.higherEntry(frame);
        if (nextEntry == null){
            return scaleMap.lastEntry().getValue();
        } else {
            return nextEntry.getValue();
        }
    }

    public Quaternionf getRotation(float frame){
        return rotationMap.floorEntry(frame).getValue();
    }

    public Quaternionf getNextRotation(float frame){
        Map.Entry<Float, Quaternionf> nextEntry = rotationMap.higherEntry(frame);
        if (nextEntry == null){
            return rotationMap.lastEntry().getValue();
        } else {
            return nextEntry.getValue();
        }
    }

    public TreeMap<Float, Vector3f> translationMap() {
        return translationMap;
    }

    public TreeMap<Float, Vector3f> scaleMap() {
        return scaleMap;
    }

    public TreeMap<Float, Quaternionf> rotationMap() {
        return rotationMap;
    }
}
