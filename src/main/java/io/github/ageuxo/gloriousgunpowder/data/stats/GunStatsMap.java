package io.github.ageuxo.gloriousgunpowder.data.stats;

import io.github.ageuxo.gloriousgunpowder.data.Material;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GunStatsMap {
    private final Map<GunStat, Map<GunStatModifier.Type, List<GunStatModifier>>> modifiers = new Object2ObjectOpenHashMap<>();
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
            throw new RuntimeException("Attempted to add modifier " + modifier + " to GunStatsMap missing GunStat "+ gunStat);
        } else {
            modifiers.get(gunStat).get(modifier.type()).add(modifier);
            needsUpdating.add(gunStat);
        }
    }

    public void addModifiers(Material material){
        for (Material.Entry entry : material.entries()){
            for (GunStatModifier modifier : entry.modifiers()){
                addModifier(modifier);
            }
        }
    }

    public void removeModifier(GunStatModifier modifier){
        modifiers.get(modifier.gunStat()).get(modifier.type()).remove(modifier);
        needsUpdating.add(modifier.gunStat());
    }

    public void removeModifiers(Material material){
        for (Material.Entry entry : material.entries()){
            for (GunStatModifier modifier : entry.modifiers()){
                removeModifier(modifier);
            }
        }
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
