package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;
import java.util.UUID;

public record TransmissionEvent(Optional<UUID> source, UUID target, Strain strain) {
    public static final Codec<TransmissionEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    UUIDUtil.CODEC.optionalFieldOf("source").forGetter(TransmissionEvent::source),
                    UUIDUtil.CODEC.fieldOf("target").forGetter(TransmissionEvent::target),
                    Strain.CODEC.fieldOf("strain").forGetter(TransmissionEvent::strain))
            .apply(instance, TransmissionEvent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TransmissionEvent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), TransmissionEvent::source,
            UUIDUtil.STREAM_CODEC, TransmissionEvent::target,
            Strain.STREAM_CODEC, TransmissionEvent::strain,
            TransmissionEvent::new);
}