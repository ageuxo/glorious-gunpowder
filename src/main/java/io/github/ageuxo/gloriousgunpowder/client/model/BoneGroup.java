package io.github.ageuxo.gloriousgunpowder.client.model;

import com.mojang.datafixers.util.Either;
import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.gloriousgunpowder.client.render.BoneGroupTransform;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BoneGroup {
    public static final Codec<BoneGroup> CODEC = Codec.recursive(
            "BoneGroup",
            groupCodec -> RecordCodecBuilder.create(
                    instance -> instance.group(
                            Codec.STRING.fieldOf("name").forGetter(BoneGroup::name),
                            ExtraCodecs.VECTOR3F.fieldOf("origin").forGetter(BoneGroup::origin),
                            Codec.either(Codec.INT, groupCodec).listOf().fieldOf("children").forGetter(BoneGroup::children)
                    ).apply(instance, BoneGroup::new)));
    public static final Codec<List<BoneGroup>> LIST_CODEC = CODEC.listOf();
    private final String name;
    private final Vector3f origin;
    private final List<Either<Integer, BoneGroup>> children;
    private final List<BoneGroup> childBones = new ArrayList<>();
    private final List<Integer> elementIndices = new ArrayList<>();
    private final BoneGroupTransform transform = new BoneGroupTransform();
    private BakedModel bakedModel;

    public BoneGroup(String name, Vector3f origin, List<Either<Integer, BoneGroup>> children) {
        this.name = name;
        this.origin = origin;
        this.children = children;
        children.forEach( entry->
                entry.ifLeft(elementIndices::add)
                        .ifRight(childBones::add));
    }

    public List<BlockElement> bindElements(List<BlockElement> elementList){
        List<BlockElement> ret = new ArrayList<>();
        for (int index : elementIndices){
            BlockElement element = elementList.get(index);
            if (element != null){
                ret.add(element);
            } else {
                throw new IllegalStateException("BoneGroup attempted to bind element but found null.");
            }
        }
        return ret;
    }

    public String name() {
        return name;
    }

    public Vector3f origin() {
        return origin;
    }

    public List<Either<Integer, BoneGroup>> children() {
        return children;
    }

    public List<BoneGroup> getChildGroups() {
        return childBones;
    }

    public void setBakedModel(BakedModel bakedModel) {
        this.bakedModel = bakedModel;
    }

    public BakedModel getBakedModel() {
        return bakedModel;
    }

    public List<BoneGroup> unrollGroups(){
        List<BoneGroup> unrolled = new ArrayList<>();
        recursiveUnroll(unrolled, this);
        return unrolled;
    }

    private void recursiveUnroll(List<BoneGroup> unrolled, BoneGroup group){
        unrolled.add(group);
        unrollSubGroups(unrolled, group);
    }

    private void unrollSubGroups(List<BoneGroup> unrolled, BoneGroup group){
        for (BoneGroup subGroup : group.getChildGroups()){
            recursiveUnroll(unrolled, subGroup);
        }
    }

    public Transformation buildTransform() {
        return transform.build(this);
    }

    public BoneGroupTransform getTransform(){
        return this.transform;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BoneGroup) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.origin, that.origin) &&
                Objects.equals(this.children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, origin, children);
    }

    @Override
    public String toString() {
        return "BoneGroup[" +
                "name=" + name + ", " +
                "origin=" + origin + ", " +
                "children=" + children + ']';
    }

}
