package io.github.ageuxo.gloriousgunpowder.data.stats;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.GunRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;

public record GunStat(String name, double defaultValue) {

    public static final ResourceKey<Registry<GunStat>> KEY = ResourceKey.createRegistryKey(GloriousGunpowderMod.rl("gunstat"));
    public static final RegistryFixedCodec<GunStat> CODEC = RegistryFixedCodec.create(GunStat.KEY);

    public GunStatModifier additionMod(String id, double value){
        return new GunStatModifier(this, GunStatModifier.Type.ADDITION, id, value);
    }

    public GunStatModifier multiplierMod(String id, double value){
        return new GunStatModifier(this, GunStatModifier.Type.MULTIPLIER, id, value);
    }

    public static Registry<GunStat> getRegistry() {
        return GunRegistries.GUN_STATS;
    }

    public Holder<GunStat> getHolder(){
        return getRegistry().wrapAsHolder(this);
    }


}
