package io.github.ageuxo.gloriousgunpowder.network;

import io.github.ageuxo.gloriousgunpowder.network.payload.AnimSyncPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PayloadRegister {
    public static final String VERSION = "1"; //Bump this when doing networking changes

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar(VERSION);

        registrar.playToClient(
                AnimSyncPayload.TYPE,
                AnimSyncPayload.STREAM_CODEC,
                ClientPayloadHandler::handleAnimSync
        );
    }
}
