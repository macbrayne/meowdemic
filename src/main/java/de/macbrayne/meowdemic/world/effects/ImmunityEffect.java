package de.macbrayne.meowdemic.world.effects;

import de.macbrayne.meowdemic.world.attachments.entity.ImmunityAttachment;
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
        ImmunityAttachment.get(entity).remove();
        super.onEffectRemoved(effectInstance, entity);
    }
}
