package de.macbrayne.meowdemic.world.effects;

import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.entity.IncubationAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class IncubationEffect extends MobEffect {
    public IncubationEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF0000);
    }

    @Override
    public void onEffectRemoved(MobEffectInstance effectInstance, LivingEntity entity) {
        if(IncubationAttachment.get(entity).getOptional().isEmpty()) return;
        TransmissionEvent event = IncubationAttachment.get(entity).getOptional().get();
        if(!entity.level().isClientSide()) {
            IncubationAttachment.get(entity).remove();
            TransmissionAttachment.get(entity).infect(event);
        }

        super.onEffectRemoved(effectInstance, entity);
    }
}
