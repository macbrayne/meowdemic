package de.macbrayne.meowdemic.effects;

import de.macbrayne.meowdemic.attachments.entity.ImmunityComponent;
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
        Strain event = entity.getAttached(ImmunityComponent.TYPE);
        if(event != null) {
            entity.removeAttached(ImmunityComponent.TYPE);
        }

        super.onEffectRemoved(effectInstance, entity);
    }
}
