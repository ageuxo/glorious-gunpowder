package io.github.ageuxo.gloriousgunpowder.anim;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AnimatableTracker extends SavedData {
    private static final Factory<AnimatableTracker> FACTORY = new Factory<>(AnimatableTracker::new, AnimatableTracker::new, null);
    private static final String DATA_KEY = "glorious_gunpowder_animatable_id_store";
    private static final String NBT_KEY = "last_id";
    private long lastId = 0;

    private AnimatableTracker() {
    }

    private AnimatableTracker(CompoundTag tag, HolderLookup.Provider lookup) {
        this.lastId = tag.getLong(NBT_KEY);
    }

    public static long makeId(ServerLevel level){
        return getInstance(level).next();
    }

    private static AnimatableTracker getInstance(ServerLevel level){
        return level.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, DATA_KEY);
    }

    @Override
    public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putLong(NBT_KEY, this.lastId);

        return pTag;
    }

    private long next(){
        setDirty();
        return ++this.lastId;
    }
}
