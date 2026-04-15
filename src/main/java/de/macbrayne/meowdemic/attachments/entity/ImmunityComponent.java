package de.macbrayne.meowdemic.attachments.entity;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.attachments.Attachments;
import de.macbrayne.meowdemic.data.Strain;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class ImmunityComponent {
    public static ImmunityData get(LivingEntity target) {
        return new ImmunityData(target);
    }

    public record ImmunityData(LivingEntity target) {
        public Optional<Strain> getOptional() {
            return Optional.ofNullable(target.getAttached(Attachments.IMMUNITY));
        }

        public void setIfNone(Strain strain) {
            if(target.getAttached(Attachments.IMMUNITY) != null) return;

            MobEffectInstance effect = new MobEffectInstance(Meowdemic.IMMUNE,
                    (int)(60 * 20 * strain.immunityFactor()),
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(Attachments.IMMUNITY, strain);
            PlayerStatsComponent.get(target).increaseTimesCured();
        }
    }
}
