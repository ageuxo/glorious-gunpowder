package io.github.ageuxo.gloriousgunpowder.data.stats;

import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.*;

public class GunStatsMap {
    private final Map<GunStat, Map<GunStatModifier.Type, List<GunStatModifier>>> modifiers = new Object2ObjectOpenHashMap<>();
    private final Map<String, GunStatModifier> modifiersById = new HashMap<>();
    private final Object2DoubleOpenHashMap<GunStat> cache = new Object2DoubleOpenHashMap<>();
    private final List<GunStat> needsUpdating = new ArrayList<>();

    public GunStatsMap(GunStat... gunStats){
        for (GunStat gunStat : gunStats){
            modifiers.put(gunStat, new EnumMap<>(GunStatModifier.Type.class));
        }
    }

    public void addModifier(GunStatModifier modifier){
        GunStat gunStat = modifier.gunStat();
        if (!modifiers.containsKey(gunStat)){
            throw new RuntimeException("Attempted to add modifier " + modifier.id() + " to GunStatsMap missing GunStat "+ gunStat);
        } else {
            modifiers.get(gunStat).get(modifier.type()).add(modifier);
            modifiersById.put(modifier.id(), modifier);
            needsUpdating.add(gunStat);
        }
    }

    public void removeModifier(GunStatModifier modifier){
        modifiers.get(modifier.gunStat()).get(modifier.type()).remove(modifier);
        modifiersById.remove(modifier.id());
        needsUpdating.add(modifier.gunStat());
    }

    public void removeModifier(String id){
        GunStatModifier modifier = modifiersById.get(id);
        removeModifier(modifier);
    }

    public double getValue(GunStat gunStat){
        if (needsUpdating.contains(gunStat)){
            compute(gunStat);
        }
        return cache.getDouble(gunStat);
    }

    private void compute(GunStat gunStat){
        double value = gunStat.defaultValue();
        //Addition
        Map<GunStatModifier.Type, List<GunStatModifier>> typeListMap = modifiers.get(gunStat);
        for (GunStatModifier addMod : typeListMap.get(GunStatModifier.Type.ADDITION)){
            value += addMod.value();
        }
        //Multipliers
        double mult = 1;
        for (GunStatModifier multMod : typeListMap.get(GunStatModifier.Type.MULTIPLIER)){
            mult += multMod.value();
        }

        cache.put(gunStat, value * mult);

    }


}
