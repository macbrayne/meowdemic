package de.macbrayne.meowdemic.attachments.entity;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class TransmissionComponent {
    public static final AttachmentType<TransmissionEvent> TYPE = AttachmentRegistry.create(Meowdemic.id("transmission"), builder -> builder
            .persistent(TransmissionEvent.CODEC));

    public static TransmissionData get(LivingEntity target) {
        return new TransmissionData(target);
    }

    public record TransmissionData(LivingEntity target) {
        public Optional<TransmissionEvent> getOptional() {
            return Optional.ofNullable(target.getAttached(TYPE));
        }

        public void setIfNone(TransmissionEvent transmissionEvent) {
            if(target.getAttached(TYPE) != null) return;

            MobEffectInstance effect = new MobEffectInstance(Meowdemic.EFFECT,
                    (int)(60 * 20 * transmissionEvent.strain().recoveryFactor()),
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(TYPE, transmissionEvent);
        }
    }
}
