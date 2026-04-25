package de.macbrayne.meowdemic.world.attachments.entity;

import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.world.attachments.ServerStatsAttachment;
import de.macbrayne.meowdemic.world.effects.MeowdemicEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class TransmissionAttachment {
    private static final RandomSource random = RandomSource.create();

    public static TransmissionData get(LivingEntity target) {
        return new TransmissionData(target);
    }

    public record TransmissionData(LivingEntity target) {
        public Optional<TransmissionEvent> getOptional() {
            return Optional.ofNullable(target.getAttached(Attachments.TRANSMISSION));
        }

        public void infect(TransmissionEvent transmissionEvent) {
            if(Attachments.isAffected(target)) return;

            MobEffectInstance effect = new MobEffectInstance(MeowdemicEffects.INFECTED,
                    (int)(60 * 20 * transmissionEvent.strain().recoveryFactor() * (random.nextGaussian() * 0.5 + 1)),
                    0, false, false, false);
            target.setAttached(Attachments.TRANSMISSION, transmissionEvent);
            target.addEffect(effect);
        }

        public void mutate(UnaryOperator<Strain> operator) {
            ServerStatsAttachment.get((ServerLevel) target.level()).addStrainsCreated();
            target.modifyAttached(Attachments.TRANSMISSION, transmissionEvent -> TransmissionEvent.mutate(transmissionEvent, operator));
        }

        public boolean hasSymptom(Symptoms symptom) {
            Optional<TransmissionEvent> event = getOptional();
            return event.isPresent() && event.get().strain().symptoms().contains(symptom);
        }

        public void remove() {
            target.removeAttached(Attachments.TRANSMISSION);
        }
    }
}
