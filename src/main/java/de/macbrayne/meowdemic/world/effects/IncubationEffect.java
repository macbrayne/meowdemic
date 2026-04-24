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

    public static void onRemove(MobEffectInstance effectInstance, LivingEntity entity) {
        if(IncubationAttachment.get(entity).getOptional().isEmpty()) return;
        if(!entity.level().isClientSide()) {
            TransmissionEvent event = IncubationAttachment.get(entity).getOptional().get();
            IncubationAttachment.get(entity).remove();
            TransmissionAttachment.get(entity).infect(event);
        }
    }
}
