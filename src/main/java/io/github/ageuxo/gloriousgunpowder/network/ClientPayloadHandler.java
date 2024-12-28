package io.github.ageuxo.gloriousgunpowder.network;

import io.github.ageuxo.gloriousgunpowder.network.payload.AnimSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleAnimSync(AnimSyncPayload load, IPayloadContext context){
        context.enqueueWork(()-> {
            //TODO sync the animation via the SyncManager probably
        });
    }
}
