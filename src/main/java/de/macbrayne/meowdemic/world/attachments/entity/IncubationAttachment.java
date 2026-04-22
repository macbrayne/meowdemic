package de.macbrayne.meowdemic.world.attachments.entity;

import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.world.effects.MeowdemicEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class IncubationAttachment {
    public static IncubationData get(LivingEntity target) {
        return new IncubationData(target);
    }

    public record IncubationData(LivingEntity target) {
        public Optional<TransmissionEvent> getOptional() {
            return Optional.ofNullable(target.getAttached(Attachments.INCUBATION));
        }

        public boolean tryIncubate(TransmissionEvent event) {
            if(target.getAttached(Attachments.INCUBATION) != null) return false;
            if(target.getAttached(Attachments.TRANSMISSION) != null) return false;
            if(target.getAttached(Attachments.IMMUNITY) != null) return false;
            if(target.getRandom().nextDouble() > event.strain().transmissionFactor()) return false;

            MobEffectInstance effect = new MobEffectInstance(MeowdemicEffects.INCUBATING,
                    (int)(60 * 20 * event.strain().transmissionFactor()), //TODO: Move to incubation factor
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(Attachments.INCUBATION, event);
            return true;
        }

        public void remove() {
            target.removeAttached(Attachments.INCUBATION);
        }
    }
}
