package io.github.ageuxo.gloriousgunpowder.client.anim;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.gloriousgunpowder.client.model.BoneGroup;
import io.github.ageuxo.gloriousgunpowder.client.model.GroupsModel;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
public class AnimatableInstance {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, Either<BakedModel, GroupsModel>> name2ModelMap = new HashMap<>();
    private final Map<String, String> animGetterMap = new HashMap<>();
    private final ArrayDeque<Animation> queue = new ArrayDeque<>();

    private final long id;

    private Animation currentAnimation = Animation.EMPTY;
    private long startTick;

    public AnimatableInstance(long id) {
        this.id = id;
    }

    public long id(){
        return this.id;
    }

    public BoneGroupTransform setGroupTransformForTick(BoneGroupTransform groupTransform, BoneGroup group, long gameTime, float partialTick){
        int tick = (int) (gameTime - startTick);
        GroupAnimationData anim = currentAnimation().getGroupData(group);
        if (anim != null){
            return groupTransform.setLerped(anim, tick, partialTick);
        }
        KeyTransform key = KeyTransform.EMPTY;
        return groupTransform.set(key.position(), key.scale(), key.rotation());
    }

    public Animation currentAnimation(){
        return this.currentAnimation;
    }

    public void nextAnimation(long gameTime){
        Animation next = queue.poll();
        if (next != null){
            this.currentAnimation = next;
            this.startTick = gameTime;
        }
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
            animGetterMap.put(entry.getKey(), entry.getValue().getPath());
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
