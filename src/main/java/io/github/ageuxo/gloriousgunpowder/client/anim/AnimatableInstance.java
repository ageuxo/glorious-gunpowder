package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import io.github.ageuxo.gloriousgunpowder.client.model.GroupsModel;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
public class AnimatableInstance {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, Either<BakedModel, GroupsModel>> name2ModelMap = new HashMap<>();
    private final ArrayDeque<Animation> queue = new ArrayDeque<>();

    private final long id;

    private Animation currentAnimation = Animation.EMPTY;
    private long startTick = -1;

    public AnimatableInstance(long id) {
        this.id = id;
    }

    public long id(){
        return this.id;
    }

    public void playAnimation(@NotNull Animation animation, long gameTime){
        this.currentAnimation = animation;
        this.startTick = gameTime;
    }

    public void playAnimation(@NotNull ResourceLocation holderLoc, @NotNull String animName, long gameTime){
        AnimationHolder holder = AnimationManager.INSTANCE.get(holderLoc);
        if (holder == null){
            LOGGER.error("Called playAnimation() on non-existent AnimationHolder: {} with animName of {}", holderLoc, animName);
        } else {
            Animation animation = holder.get(animName);
            if (animation == null){
                LOGGER.warn("Tried to play missing animation {} of AnimationHolder {}", animName, holderLoc);
            } else {
                playAnimation(animation, gameTime);
            }
        }
    }

    /**
     * Main getter for Animation
     * @param gameTime The logical tick of this instance
     * @return Animation that should be playing
     */
    public Animation currentOrNextAnimation(long gameTime){
        int tick = getTick(gameTime);
        float length = this.currentAnimation.length();
        if ((length <= 0) || (length < (tick / 20f))) {
            this.startTick = gameTime;
            this.currentAnimation = nextOrEmpty();
        }
        return this.currentAnimation;
    }

    public BoneGroupTransform setGroupTransformForTick(BoneGroupTransform groupTransform, BoneGroup group, long gameTime, float partialTick){
        int tick = getTick(gameTime);
        GroupAnimationData anim = currentOrNextAnimation(tick).getGroupData(group);
        if (anim != null){
            return groupTransform.setLerped(anim, tick, partialTick);
        }
        KeyTransform key = KeyTransform.EMPTY;
        return groupTransform.set(key.position(), key.scale(), key.rotation());
    }

    private int getTick(long gameTime) {
        return startTick >= 0 ? (int) (gameTime - startTick) : 0;
    }

    private Animation nextOrEmpty(){
        Animation polled = this.queue.poll();
        return polled != null ? polled : Animation.EMPTY;
    }

    public void addAnimToQueue(Animation animation){
        AnimationManager.INSTANCE.get(GloriousGunpowderMod.rl("matchlock"));
    }

    @Nullable
    public Either<BakedModel, GroupsModel> getEitherModel(String boneName){
        return this.name2ModelMap.get(boneName);
    }

    public void addModel(String key, Either<BakedModel, GroupsModel> model){
        this.name2ModelMap.put(key, model);
    }

    public void populateInstance(Map<String, ResourceLocation> components) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
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


}
