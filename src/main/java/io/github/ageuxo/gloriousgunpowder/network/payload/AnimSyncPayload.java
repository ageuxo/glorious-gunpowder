package io.github.ageuxo.gloriousgunpowder.network.payload;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record AnimSyncPayload(long instance, String animation) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AnimSyncPayload> TYPE = new Type<>(GloriousGunpowderMod.rl("anim_sync"));

    public static final StreamCodec<ByteBuf, AnimSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            AnimSyncPayload::instance,
            ByteBufCodecs.STRING_UTF8,
            AnimSyncPayload::animation,
            AnimSyncPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
