package de.macbrayne.meowdemic.world.attachments;

import com.mojang.serialization.Codec;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.PullHistoryEvent;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;

public class Attachments {
    public static final Logger LOGGER = LoggerFactory.getLogger(Meowdemic.MOD_ID);
    public static final AttachmentType<Strain> IMMUNITY = AttachmentRegistry.create(Meowdemic.id("immunity"), builder -> builder
            .persistent(Strain.CODEC));
    public static final AttachmentType<TransmissionEvent> INCUBATION = AttachmentRegistry.create(Meowdemic.id("incubation"), builder -> builder
            .persistent(TransmissionEvent.CODEC));
    public static final AttachmentType<TransmissionEvent> TRANSMISSION = AttachmentRegistry.create(Meowdemic.id("transmission"), builder -> builder
            .persistent(TransmissionEvent.CODEC)
            .syncWith(TransmissionEvent.STREAM_CODEC, AttachmentSyncPredicate.targetOnly()));

    public static class PlayerStats {
        public static final AttachmentType<Integer> ENTITIES_INFECTED = AttachmentRegistry.create(Meowdemic.id("entities_infected"), builder -> builder
                .persistent(Codec.INT)
                .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly())
                .initializer(() -> 0)
                .copyOnDeath());

        public static final AttachmentType<Integer> TIMES_CURED = AttachmentRegistry.create(Meowdemic.id("times_cured"), builder -> builder
                .persistent(Codec.INT)
                .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly())
                .initializer(() -> 0)
                .copyOnDeath());

        public static final AttachmentType<Integer> POINTS_DELTA = AttachmentRegistry.create(Meowdemic.id("points"), builder -> builder
                .persistent(Codec.INT)
                .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly())
                .initializer(() -> 0)
                .copyOnDeath());

        public static final AttachmentType<Integer> GACHA_PITY = AttachmentRegistry.create(Meowdemic.id("gacha_pity"), builder -> builder
                .persistent(Codec.INT)
                .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly())
                .initializer(() -> 0)
                .copyOnDeath());

        public static final AttachmentType<ArrayDeque<PullHistoryEvent>> PULL_HISTORY = AttachmentRegistry.create(Meowdemic.id("pull_history"), builder -> builder
                .persistent(PullHistoryEvent.DEQUE_CODEC)
                .syncWith(PullHistoryEvent.DEQUE_STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
                .initializer(() -> new ArrayDeque<>(10))
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

    public static boolean isAffected(LivingEntity entity) {
        return entity.getAttached(Attachments.INCUBATION) != null ||
                entity.getAttached(Attachments.TRANSMISSION) != null ||
                entity.getAttached(Attachments.IMMUNITY) != null;
    }
}
