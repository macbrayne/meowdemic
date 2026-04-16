package de.macbrayne.meowdemic.world.effects;

import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.data.Strain;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class ImmunityEffect extends MobEffect {

    public ImmunityEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF0000);
    }

    @Override
    public void onEffectRemoved(MobEffectInstance effectInstance, LivingEntity entity) {
        Strain event = entity.getAttached(Attachments.IMMUNITY);
        if(event != null) {
            entity.removeAttached(Attachments.IMMUNITY);
        }

        super.onEffectRemoved(effectInstance, entity);
    }
}
