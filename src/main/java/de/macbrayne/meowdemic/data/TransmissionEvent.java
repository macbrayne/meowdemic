package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.Optional;
import java.util.UUID;

public record TransmissionEvent(Optional<UUID> source, UUID target, Strain strain) {
    public static final Codec<TransmissionEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    UUIDUtil.CODEC.optionalFieldOf("source").forGetter(TransmissionEvent::source),
                    UUIDUtil.CODEC.fieldOf("target").forGetter(TransmissionEvent::target),
                    Strain.CODEC.fieldOf("strain").forGetter(TransmissionEvent::strain))
            .apply(instance, TransmissionEvent::new));
}