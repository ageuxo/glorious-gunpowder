package io.github.ageuxo.gloriousgunpowder.client.render;

import io.github.ageuxo.gloriousgunpowder.client.model.BonePartModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.model.DefaultedGeoModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BoneGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {
    private final Map<GeoBone, BonePartModel> bone2ModelMap = new HashMap<>();
    private final Map<String, BonePartModel> name2ModelMap = new HashMap<>();
    private final ResourceLocation assetSubPath;

    public BoneGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
        this.assetSubPath = assetSubpath;
    }

    public BakedModel get(GeoBone bone){
        return bone2ModelMap.get(bone);
    }

    public BonePartModel get(String bone){
        return name2ModelMap.get(bone);
    }

    public void addModel(GeoBone bone, BonePartModel model){
        bone2ModelMap.put(bone, model);
        name2ModelMap.put(bone.getName(), model);
    }

    public Set<Map.Entry<GeoBone, BonePartModel>> getEntries(){
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
}
