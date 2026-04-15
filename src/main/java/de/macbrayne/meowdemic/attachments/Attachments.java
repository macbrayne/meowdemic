package de.macbrayne.meowdemic.attachments;

import com.mojang.serialization.Codec;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
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

    public static class PlayerStats {
        public static final AttachmentType<Integer> PEOPLE_INFECTED = AttachmentRegistry.create(Meowdemic.id("people_infected"), builder -> builder
                .persistent(Codec.INT)
                .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly())
                .copyOnDeath());

        public static final AttachmentType<Integer> TIMES_CURED = AttachmentRegistry.create(Meowdemic.id("times_cured"), builder -> builder
                .persistent(Codec.INT)
                .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly())
                .copyOnDeath());

        public static void init() {
            LOGGER.info("Registered player stats attachments");
        }
    }

    public static class ServerStats {
        public static final AttachmentType<Integer> CURRENTLY_INFECTED = AttachmentRegistry.create(Meowdemic.id("currently_infected"), builder -> builder
                .persistent(Codec.INT)
                .initializer(() -> 0));

        public static final AttachmentType<Integer> TOTAL_INFECTED = AttachmentRegistry.create(Meowdemic.id("total_infected"), builder -> builder
                .persistent(Codec.INT)
                .initializer(() -> 0));

        public static final AttachmentType<Integer> SPECIES_BARRIERS_CROSSED = AttachmentRegistry.create(Meowdemic.id("species_barriers_crossed"), builder -> builder
                .persistent(Codec.INT)
                .initializer(() -> 0));

        public static final AttachmentType<Integer> STRAINS_MUTATED = AttachmentRegistry.create(Meowdemic.id("strains_mutated"), builder -> builder
                .persistent(Codec.INT)
                .initializer(() -> 0));


        public static void init() {
            LOGGER.info("Registered server stats attachments");
        }
    }

    public static void init() {
        LOGGER.info("Registered data attachments");
        PlayerStats.init();
        ServerStats.init();
    }
}
