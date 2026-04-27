package de.macbrayne.meowdemic.world.attachments.entity;

import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.world.effects.MeowdemicEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class IncubationAttachment {
    private static final RandomSource random = RandomSource.create();

    public static IncubationData get(LivingEntity target) {
        return new IncubationData(target);
    }

    public record IncubationData(LivingEntity target) {
        public Optional<TransmissionEvent> getOptional() {
            return Optional.ofNullable(target.getAttached(Attachments.INCUBATION));
        }

        public boolean tryIncubate(TransmissionEvent event) {
            if(Attachments.isAffected(target)) return false;
            if(event.strain().transmissionFactor() < random.nextFloat()) return false;

            MobEffectInstance effect = new MobEffectInstance(MeowdemicEffects.INCUBATING,
                    (int)(60 * 20 * event.strain().incubationFactor() * (random.nextGaussian() * 0.5 + 1)),
                    0, false, false, false);
            target.setAttached(Attachments.INCUBATION, event);
            target.addEffect(effect);
            return true;
        }

        public void remove() {
            target.removeAttached(Attachments.INCUBATION);
        }
    }
}
