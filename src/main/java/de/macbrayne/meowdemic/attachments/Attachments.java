package de.macbrayne.meowdemic.attachments;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Attachments {
    public static final Logger LOGGER = LoggerFactory.getLogger(Meowdemic.MOD_ID);
    public static final AttachmentType<Strain> IMMUNITY = AttachmentRegistry.create(Meowdemic.id("immunity"), builder -> builder
            .persistent(Strain.CODEC));
    public static final AttachmentType<TransmissionEvent> TRANSMISSION = AttachmentRegistry.create(Meowdemic.id("transmission"), builder -> builder
            .persistent(TransmissionEvent.CODEC)
            .syncWith(TransmissionEvent.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
            .copyOnDeath());

    public static void init() {
        LOGGER.info("Registered data attachments");
    }
}
