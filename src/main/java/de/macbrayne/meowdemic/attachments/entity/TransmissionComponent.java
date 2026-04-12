package de.macbrayne.meowdemic.attachments.entity;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class TransmissionComponent {
    public static final AttachmentType<TransmissionEvent> TYPE = AttachmentRegistry.create(Meowdemic.id("transmission"), builder -> builder
            .persistent(TransmissionEvent.CODEC));

    public static TransmissionData get(LivingEntity target) {
        return new TransmissionData(target);
    }

    public record TransmissionData(LivingEntity target) {
        public TransmissionEvent getOrSet(TransmissionEvent defaultValue) {
            return target.getAttachedOrSet(TYPE, defaultValue);
        }

        public void set(TransmissionEvent transmissionEvent) {
            MobEffectInstance effect = new MobEffectInstance(Meowdemic.EFFECT,
                    (int)(60 * 20 * transmissionEvent.strain().recoveryFactor()),
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(TYPE, transmissionEvent);
        }
    }
}
