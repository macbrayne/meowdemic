package de.macbrayne.meowdemic.world.attachments.entity;

import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.world.effects.MeowdemicEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class ImmunityAttachment {
    private static final RandomSource random = RandomSource.create();

    public static ImmunityData get(LivingEntity target) {
        return new ImmunityData(target);
    }

    public record ImmunityData(LivingEntity target) {

        public boolean setIfNone(Strain strain, float modifier) {
            if(Attachments.isAffected(target)) return false;

            MobEffectInstance effect = new MobEffectInstance(MeowdemicEffects.IMMUNE,
                    (int)(60 * 20 * strain.immunityFactor() * modifier * (random.nextGaussian() * 0.5 + 1)),
                    0, false, false, false);
            PlayerStatsAttachment.get(target).increaseTimesCured();
            target.setAttached(Attachments.IMMUNITY, strain);
            target.addEffect(effect);
            return true;
        }

        public void remove() {
            target.removeAttached(Attachments.IMMUNITY);
        }
    }
}
