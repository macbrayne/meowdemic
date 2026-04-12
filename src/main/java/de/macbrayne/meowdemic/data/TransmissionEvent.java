package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.macbrayne.meowdemic.attachments.StrainsComponent;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public record TransmissionEvent(UUID source, UUID target, Strain strain, int expiresIn) {
    public static final Codec<TransmissionEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    UUIDUtil.CODEC.fieldOf("source").forGetter(TransmissionEvent::source),
                    UUIDUtil.CODEC.fieldOf("target").forGetter(TransmissionEvent::target),
                    Strain.CODEC.fieldOf("strain").forGetter(TransmissionEvent::strain),
                    Codec.INT.fieldOf("expiresIn").forGetter(TransmissionEvent::expiresIn))
            .apply(instance, TransmissionEvent::new));

    public TransmissionEvent(UUID source, UUID target, ServerLevel level, String id, int expiresIn) {
        this(source, target, StrainsComponent.get(level).getStrain(id), expiresIn);
    }
}