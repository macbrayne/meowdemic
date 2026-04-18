package de.macbrayne.meowdemic.world.attachments.entity;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.data.Strain;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class ImmunityAttachment {
    public static ImmunityData get(LivingEntity target) {
        return new ImmunityData(target);
    }

    public record ImmunityData(LivingEntity target) {
        public Optional<Strain> getOptional() {
            return Optional.ofNullable(target.getAttached(Attachments.IMMUNITY));
        }

        public boolean setIfNone(Strain strain, float modifier) {
            if(target.getAttached(Attachments.IMMUNITY) != null) return false;

            MobEffectInstance effect = new MobEffectInstance(Meowdemic.IMMUNE,
                    (int)(60 * 20 * strain.immunityFactor() * modifier),
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(Attachments.IMMUNITY, strain);
            PlayerStatsAttachment.get(target).increaseTimesCured();
            return true;
        }

        public void remove() {
            target.removeAttached(Attachments.IMMUNITY);
        }
    }
}
