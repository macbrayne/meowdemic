package de.macbrayne.meowdemic.world.attachments.entity;

import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.world.effects.MeowdemicEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class TransmissionAttachment {

    public static TransmissionData get(LivingEntity target) {
        return new TransmissionData(target);
    }

    public record TransmissionData(LivingEntity target) {
        public Optional<TransmissionEvent> getOptional() {
            return Optional.ofNullable(target.getAttached(Attachments.TRANSMISSION));
        }

        public boolean infect(TransmissionEvent transmissionEvent) {
            if(target.getAttached(Attachments.TRANSMISSION) != null) return false;
            if(target.getAttached(Attachments.INCUBATION) != null) return false;
            if(target.getAttached(Attachments.IMMUNITY) != null) return false;

            MobEffectInstance effect = new MobEffectInstance(MeowdemicEffects.INFECTED,
                    (int)(60 * 20 * transmissionEvent.strain().recoveryFactor()),
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(Attachments.TRANSMISSION, transmissionEvent);
            return true;
        }

        public void mutate(UnaryOperator<Strain> operator) {
            target.modifyAttached(Attachments.TRANSMISSION, transmissionEvent -> TransmissionEvent.mutate(transmissionEvent, operator));
        }

        public boolean hasSymptom(Symptoms symptom) {
            Optional<TransmissionEvent> event = getOptional();
            return event.isPresent() && event.get().strain().symptoms().contains(symptom);
        }
    }
}
