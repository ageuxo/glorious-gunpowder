package io.github.ageuxo.gloriousgunpowder.data.stats;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.GunRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GunStats {
    public static final DeferredRegister<GunStat> GUN_STATS = DeferredRegister.create(GunRegistries.GUN_STATS, GloriousGunpowderMod.MOD_ID);

    public static final DeferredHolder<GunStat, GunStat> DAMAGE = GUN_STATS.register("damage",() -> new GunStat("damage", 2));
    public static final DeferredHolder<GunStat, GunStat> RELOAD_TIME = GUN_STATS.register("reload_time", ()->new GunStat("reload_time", 4));
    public static final DeferredHolder<GunStat, GunStat> ACCURACY = GUN_STATS.register("accuracy", ()->new GunStat("accuracy", 1));
    public static final DeferredHolder<GunStat, GunStat> VELOCITY = GUN_STATS.register("velocity", ()->new GunStat("velocity", 1));


}
