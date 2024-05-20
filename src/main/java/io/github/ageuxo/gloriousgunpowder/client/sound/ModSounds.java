package io.github.ageuxo.gloriousgunpowder.client.sound;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, GloriousGunpowderMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_SHOT_SOUND = SOUND_EVENTS.register("gun_shot", () -> SoundEvent.createVariableRangeEvent(GloriousGunpowderMod.rl("entity.gun_shot")));

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
