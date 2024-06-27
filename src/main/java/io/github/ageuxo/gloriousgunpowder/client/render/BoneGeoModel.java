package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.model.GroupsModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.model.DefaultedGeoModel;

import java.util.HashMap;
import java.util.Map;

public class BoneGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation EMPTY_MODEL_LOCATION = GloriousGunpowderMod.rl("empty");
    private static BakedModel EMPTY_MODEL;
    private final Map<String, Either<BakedModel, GroupsModel>> name2ModelMap = new HashMap<>();
    private final ResourceLocation assetSubPath;

    public BoneGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
        this.assetSubPath = assetSubpath;
    }

    public Either<BakedModel, GroupsModel> getEitherModel(String boneName){
        return this.name2ModelMap.get(boneName);
    }

    public void addModel(String key, Either<BakedModel, GroupsModel> model){
        this.name2ModelMap.put(key, model);
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

    public void populateGeoModel(Map<String, ResourceLocation> components) {
        var manager = Minecraft.getInstance().getModelManager();
        for (var entry : components.entrySet()){
            addModel(entry.getKey(), fetchModel(manager, entry.getValue()));
        }
    }

    public Either<BakedModel, GroupsModel> fetchModel(ModelManager manager, ResourceLocation location){
        ResourceLocation modelLocation = modelLocation(location);
        BakedModel model = manager.getModel(modelLocation);
        if (model.equals(manager.getMissingModel())){
            LOGGER.error("Fetched missing model at {}", modelLocation);
        }
        if (model instanceof GroupsModel groupsModel){
            return Either.right(groupsModel);
        }
        return Either.left(model);
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
