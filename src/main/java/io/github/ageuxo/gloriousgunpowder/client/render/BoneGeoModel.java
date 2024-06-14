package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import io.github.ageuxo.gloriousgunpowder.client.model.GroupsModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.model.DefaultedGeoModel;

import java.util.*;

public class BoneGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation EMPTY_MODEL_LOCATION = GloriousGunpowderMod.rl("empty");
    private static BakedModel EMPTY_MODEL;
    private final Map<GeoBone, BakedModel> bone2ModelMap = new HashMap<>();
    private final Map<String, BakedModel> name2ModelMap = new HashMap<>();
    private final Map<String, BoneGroup> name2GroupMap = new HashMap<>();
    private final ResourceLocation assetSubPath;

    public BoneGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
        this.assetSubPath = assetSubpath;
    }

    public BoneGroup get(GeoBone bone){
        return name2GroupMap.computeIfAbsent(bone.getName(), (name)->new BoneGroup(name, new Vector3f(), List.of()));
    }

    public BakedModel get(String bone){
        return name2ModelMap.get(bone);
    }

    public void addModel(GeoBone bone, BakedModel model){
        bone2ModelMap.put(bone, model);
        name2ModelMap.put(bone.getName(), model);
    }

    public void addBoneGroup(GeoBone bone, BoneGroup group){
        name2GroupMap.put(bone.getName(), group);
    }

    public Set<Map.Entry<GeoBone, BakedModel>> getEntries(){
        return bone2ModelMap.entrySet();
    }

    @Override
    protected String subtype() {
        return "bone";
    }

    @Override
    public Animation getAnimation(T animatable, String name) {
        String[] lookup = name.split(":");
        if (lookup.length != 2){
            throw new RuntimeException("BoneGeoModel attempted to get invalid animation. Supplied string should always contain exactly one \":\" to separate ResourceLocation path from animation name. String:\""+name+"\"");
        }
        ResourceLocation baseLocation = new ResourceLocation(this.assetSubPath.getNamespace(), lookup[0]);
        ResourceLocation location = buildFormattedAnimationPath(baseLocation);
        BakedAnimations bakedAnimations = GeckoLibCache.getBakedAnimations().get(location);

        if (bakedAnimations == null) {
            if (!location.getPath().contains("animations/"))
                throw GeckoLibConstants.exception(location, "Invalid animation resource path provided - GeckoLib animations must be placed in assets/<modid>/animations/");

            throw GeckoLibConstants.exception(location, "Unable to find animation file.");
        }

        return bakedAnimations.getAnimation(lookup[1]);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        throw geoException();
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        throw geoException();
    }

    private IllegalStateException geoException(){
        return new IllegalStateException("Do not call this method on BoneGeoModel and its subclasses. This always throws!");
    }

    public void fetchModels(T animatable, Map<String, ResourceLocation> map) {
        BakedGeoModel bakedModel = getBakedModel(getModelResource(animatable));
        List<GeoBone> topBones = bakedModel.topLevelBones();
        ModelManager manager = Minecraft.getInstance().getModelManager();

        GeoBone root = topBones.getFirst();
        for (GeoBone bone : root.getChildBones()){
            ResourceLocation location = map.get(bone.getName());
            if (location != null){
                addModel(bone, manager.getModel(modelLocation(location)));
            } else {
                LOGGER.error("Attempted to fetch model from null ResLoc for bone: {}", bone.getName());
            }
        }

        addModel(root, getEmptyModel());

    }

    public void fetchBones(){
        Set<Map.Entry<GeoBone, BakedModel>> geoBones = bone2ModelMap.entrySet();
        for (Map.Entry<GeoBone, BakedModel> entry : geoBones){
            if (entry.getValue() instanceof GroupsModel groupsModel){
                List<BoneGroup> topLevelGroups = groupsModel.getTopLevelGroups();
                List<BoneGroup> groups = new ArrayList<>(topLevelGroups);
                for (BoneGroup group : topLevelGroups){
                    groups.addAll(group.unrollGroups());
                }
                recursiveBind(entry.getKey(), groups);
            }
        }
    }

    private void recursiveBind(GeoBone bone, List<BoneGroup> unrolledGroups){
        bindBoneToGroup(bone, unrolledGroups);
        bindSubBones(bone, unrolledGroups);
    }

    private void bindBoneToGroup(GeoBone bone, List<BoneGroup> unrolledGroups){
        String boneName = bone.getName();
        for (BoneGroup group : unrolledGroups) {
            if (group.name().equalsIgnoreCase(boneName)) {
                addBoneGroup(bone, group);
                return;
            }
        }
    }

    private void bindSubBones(GeoBone bone, List<BoneGroup> unrolledGroups){
        for (GeoBone subBone : bone.getChildBones()){
            recursiveBind(subBone, unrolledGroups);
        }
    }


    private ResourceLocation modelLocation(ResourceLocation location){
        return location.withPrefix("gun_part/");
    }

    private BakedModel getEmptyModel(){
        ModelManager manager = Minecraft.getInstance().getModelManager();
        if (EMPTY_MODEL == null){
            BakedModel empty = manager.getModel(modelLocation(EMPTY_MODEL_LOCATION));
            EMPTY_MODEL = empty;
            return empty;
        }
        return EMPTY_MODEL;
    }

}
