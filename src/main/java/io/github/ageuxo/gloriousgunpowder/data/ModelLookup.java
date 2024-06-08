package io.github.ageuxo.gloriousgunpowder.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModelLookup {
    public static final Codec<Map<String, ResourceLocation>> CODEC = Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC);
    public static final StreamCodec<ByteBuf, Map<String, ResourceLocation>> STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ResourceLocation.STREAM_CODEC);

}
