package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;

public record PullHistoryEvent(Upgrades upgrade, Instant timeReceived) {
    public static final Codec<PullHistoryEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Upgrades.CODEC.fieldOf("upgrade").forGetter(PullHistoryEvent::upgrade),
            Codec.LONG.fieldOf("timeReceived").xmap(Instant::ofEpochSecond, Instant::getEpochSecond).forGetter(PullHistoryEvent::timeReceived)
    ).apply(instance, PullHistoryEvent::new));
    public static final Codec<ArrayDeque<PullHistoryEvent>> DEQUE_CODEC = CODEC.listOf().xmap(ArrayDeque::new, ArrayList::new);

    public static final StreamCodec<ByteBuf, PullHistoryEvent> STREAM_CODEC = StreamCodec.composite(Upgrades.STREAM_CODEC, PullHistoryEvent::upgrade, ByteBufCodecs.VAR_LONG.map(Instant::ofEpochSecond, Instant::getEpochSecond), PullHistoryEvent::timeReceived, PullHistoryEvent::new);
    public static final StreamCodec<ByteBuf, ArrayDeque<PullHistoryEvent>> DEQUE_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list()).map(ArrayDeque::new, ArrayList::new);
}
