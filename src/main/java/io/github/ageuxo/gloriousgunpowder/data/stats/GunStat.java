package io.github.ageuxo.gloriousgunpowder.data.stats;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.GunRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;

public record GunStat(String name, float defaultValue) {

    public static final ResourceKey<Registry<GunStat>> KEY = ResourceKey.createRegistryKey(GloriousGunpowderMod.rl("gunstat"));

    public GunStatModifier additionMod(float value){
        return new GunStatModifier(this, GunStatModifier.Type.ADDITION, value);
    }

    public GunStatModifier multiplierMod(float value){
        return new GunStatModifier(this, GunStatModifier.Type.MULTIPLIER, value);
    }

    public static Registry<GunStat> getRegistry() {
        return GunRegistries.GUN_STATS;
    }

    public Holder<GunStat> getHolder(){
        return getRegistry().wrapAsHolder(this);
    }


}
