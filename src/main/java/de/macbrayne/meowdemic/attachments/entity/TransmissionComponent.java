package de.macbrayne.meowdemic.attachments.entity;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class TransmissionComponent {
    public static final AttachmentType<TransmissionEvent> TYPE = AttachmentRegistry.create(Meowdemic.id("transmission"), builder -> builder
            .persistent(TransmissionEvent.CODEC));

    public static TransmissionData get(AttachmentTarget target) {
        return new TransmissionData(target);
    }

    public record TransmissionData(AttachmentTarget target) {
        public TransmissionEvent getOrSet(TransmissionEvent defaultValue) {
            return target.getAttachedOrSet(TYPE, defaultValue);
        }

        public void set(TransmissionEvent transmissionEvent) {
            target.setAttached(TYPE, transmissionEvent);
        }
    }
}
