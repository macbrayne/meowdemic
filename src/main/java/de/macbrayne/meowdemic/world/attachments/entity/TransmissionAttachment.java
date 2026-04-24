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
            if(target.getAttached(Attachments.TRANSMISSION) != null) return;
            if(target.getAttached(Attachments.INCUBATION) != null) return;
            if(target.getAttached(Attachments.IMMUNITY) != null) return;

            MobEffectInstance effect = new MobEffectInstance(MeowdemicEffects.INFECTED,
                    (int)(60 * 20 * transmissionEvent.strain().recoveryFactor() * (random.nextGaussian() * 0.5 + 1)),
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(Attachments.TRANSMISSION, transmissionEvent);
        }

        public void mutate(UnaryOperator<Strain> operator) {
            ServerStatsAttachment.get((ServerLevel) target.level()).addStrainsCreated();
            target.modifyAttached(Attachments.TRANSMISSION, transmissionEvent -> TransmissionEvent.mutate(transmissionEvent, operator));
        }

        public boolean hasSymptom(Symptoms symptom) {
            Optional<TransmissionEvent> event = getOptional();
            return event.isPresent() && event.get().strain().symptoms().contains(symptom);
        }
    }
}
