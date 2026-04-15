package de.macbrayne.meowdemic.attachments.entity;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.attachments.Attachments;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class TransmissionComponent {

    public static TransmissionData get(LivingEntity target) {
        return new TransmissionData(target);
    }

    public record TransmissionData(LivingEntity target) {
        public Optional<TransmissionEvent> getOptional() {
            return Optional.ofNullable(target.getAttached(Attachments.TRANSMISSION));
        }

        public boolean tryInfecting(TransmissionEvent transmissionEvent) {
            if(target.getAttached(Attachments.TRANSMISSION) != null) return false;
            if(target.getAttached(Attachments.IMMUNITY) != null) return false;
            if(target.getRandom().nextDouble() > transmissionEvent.strain().transmissionFactor()) return false;

            MobEffectInstance effect = new MobEffectInstance(Meowdemic.INFECTED,
                    (int)(60 * 20 * transmissionEvent.strain().recoveryFactor()),
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(Attachments.TRANSMISSION, transmissionEvent);
            return true;
        }

        public boolean hasSymptom(Symptoms symptom) {
            Optional<TransmissionEvent> event = getOptional();
            return event.isPresent() && event.get().strain().symptoms().contains(symptom);
        }
    }
}
