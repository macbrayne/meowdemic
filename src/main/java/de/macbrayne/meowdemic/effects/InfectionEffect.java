package de.macbrayne.meowdemic.effects;

import de.macbrayne.meowdemic.attachments.entity.ImmunityComponent;
import de.macbrayne.meowdemic.attachments.entity.TransmissionComponent;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class InfectionEffect extends MobEffect {
    public InfectionEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF0000);
    }

    @Override
    public void onEffectAdded(MobEffectInstance effectInstance, LivingEntity entity) {
        super.onEffectAdded(effectInstance, entity);
    }

    @Override
    public void onEffectRemoved(MobEffectInstance effectInstance, LivingEntity entity) {
        TransmissionEvent event = entity.getAttached(TransmissionComponent.TYPE);
        if(event != null) {
            entity.removeAttached(TransmissionComponent.TYPE);
            ImmunityComponent.get(entity).setIfNone(event.strain());
        }

        super.onEffectRemoved(effectInstance, entity);
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }
}
