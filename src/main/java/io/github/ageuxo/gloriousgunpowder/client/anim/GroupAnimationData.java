package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;
import java.util.TreeMap;

public class GroupAnimationData {
    public static final Codec<Integer> KEY_CODEC = Codec.FLOAT.xmap(f -> (int) f.floatValue() * 20, i -> i / 20f);
    public static final Codec<TreeMap<Integer, Vector3f>> VEC3_ENTRY_CODEC = Codec.unboundedMap(KEY_CODEC, ExtraCodecs.VECTOR3F).xmap(TreeMap::new, Map::copyOf);
    public static final Codec<TreeMap<Integer, Quaternionf>> QUAT_ENTRY_CODEC = Codec.unboundedMap(KEY_CODEC, ExtraCodecs.VECTOR3F.xmap(vec -> new Quaternionf(vec.x(), vec.y(), vec.z(), 0), quat -> new Vector3f(quat.x(), quat.y(), quat.z()))).xmap(TreeMap::new, Map::copyOf);
    public static final Codec<GroupAnimationData> CODEC = RecordCodecBuilder.create(instance->instance.group(
        VEC3_ENTRY_CODEC.optionalFieldOf("position", new TreeMap<>()).forGetter(GroupAnimationData::translationMap),
        VEC3_ENTRY_CODEC.optionalFieldOf("scale", new TreeMap<>()).forGetter(GroupAnimationData::scaleMap),
        QUAT_ENTRY_CODEC.optionalFieldOf("rotation", new TreeMap<>()).forGetter(GroupAnimationData::rotationMap)
    ).apply(instance, GroupAnimationData::new));
    private final TreeMap<Integer, Vector3f> translationMap;
    private final TreeMap<Integer, Vector3f> scaleMap;
    private final TreeMap<Integer, Quaternionf> rotationMap;
    ResourceLocation key;

    public GroupAnimationData(TreeMap<Integer, Vector3f> translationMap, TreeMap<Integer, Vector3f> scaleMap, TreeMap<Integer, Quaternionf> rotationMap) {
        this.translationMap = translationMap;
        this.scaleMap = scaleMap;
        this.rotationMap = rotationMap;
    }

    public Vector3f getTranslation(int frame){
        return translationMap.floorEntry(frame).getValue();
    }

    public Vector3f getNextTranslation(int frame){
        Map.Entry<Integer, Vector3f> nextEntry = translationMap.higherEntry(frame);
        if (nextEntry == null){
            return translationMap.lastEntry().getValue();
        } else {
            return nextEntry.getValue();
        }
    }

    public Vector3f getScale(int frame){
        return scaleMap.floorEntry(frame).getValue();
    }

    public Vector3f getNextScale(int frame){
        Map.Entry<Integer, Vector3f> nextEntry = scaleMap.higherEntry(frame);
        if (nextEntry == null){
            return scaleMap.lastEntry().getValue();
        } else {
            return nextEntry.getValue();
        }
    }

    public Quaternionf getRotation(int frame){
        return rotationMap.floorEntry(frame).getValue();
    }

    public Quaternionf getNextRotation(int frame){
        Map.Entry<Integer, Quaternionf> nextEntry = rotationMap.higherEntry(frame);
        if (nextEntry == null){
            return rotationMap.lastEntry().getValue();
        } else {
            return nextEntry.getValue();
        }
    }

    public TreeMap<Integer, Vector3f> translationMap() {
        return translationMap;
    }

    public TreeMap<Integer, Vector3f> scaleMap() {
        return scaleMap;
    }

    public TreeMap<Integer, Quaternionf> rotationMap() {
        return rotationMap;
    }

    public ResourceLocation key(){
        return this.key;
    }
}
