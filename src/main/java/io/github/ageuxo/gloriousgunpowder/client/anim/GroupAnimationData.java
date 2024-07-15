package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.TreeMap;

public class GroupAnimationData {
    public static final Codec<GroupAnimationData> CODEC = Codec.unboundedMap(Codec.INT, KeyTransform.CODEC).xmap(GroupAnimationData::new, GroupAnimationData::keyFrameMap);
    private final TreeMap<Integer, KeyTransform> keyFrameMap;
    ResourceLocation key;

    public GroupAnimationData(Map<Integer, KeyTransform> keyFrameMap) {
        this(keyFrameMap instanceof TreeMap<Integer, KeyTransform> treeMap ? treeMap : new TreeMap<>(keyFrameMap));
    }

    public GroupAnimationData(TreeMap<Integer, KeyTransform> keyFrameMap) {
        this.keyFrameMap = keyFrameMap;
    }

    public KeyTransform get(int frame){
        return keyFrameMap.floorEntry(frame).getValue();
    }

    public KeyTransform getNext(int frame){
        Map.Entry<Integer, KeyTransform> nextEntry = keyFrameMap.higherEntry(frame);
        if (nextEntry == null){
            return keyFrameMap.lastEntry().getValue();
        } else {
            return nextEntry.getValue();
        }
    }

    public TreeMap<Integer, KeyTransform> keyFrameMap(){
        return keyFrameMap;
    }

    public ResourceLocation key(){
        return this.key;
    }
}
