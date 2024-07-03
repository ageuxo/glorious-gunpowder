package io.github.ageuxo.gloriousgunpowder.client;

import io.github.ageuxo.gloriousgunpowder.client.anim.AnimationManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class ClientBusEvents {
    @SubscribeEvent
    public static void registerReloadListener(AddReloadListenerEvent event){
        event.addListener(AnimationManager.INSTANCE);
    }
}
