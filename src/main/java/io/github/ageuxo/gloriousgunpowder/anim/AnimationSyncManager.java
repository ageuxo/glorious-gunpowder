package io.github.ageuxo.gloriousgunpowder.anim;

import io.github.ageuxo.gloriousgunpowder.network.payload.AnimSyncPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public class AnimationSyncManager {
    public static void syncAnimation(Entity entity, long instanceId, String animation){
        PacketDistributor.sendToPlayersTrackingEntity(entity, new AnimSyncPayload(instanceId, animation));
    }
}
