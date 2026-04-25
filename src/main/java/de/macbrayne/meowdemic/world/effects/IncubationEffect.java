package de.macbrayne.meowdemic.world.effects;

import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.entity.IncubationAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class IncubationEffect extends MobEffect {
    public IncubationEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF0000);
    }

    public static void onRemove(MobEffectInstance effectInstance, LivingEntity entity) {
        Optional<TransmissionEvent> event = IncubationAttachment.get(entity).getOptional();
        if(event.isPresent() && !entity.level().isClientSide()) {
            IncubationAttachment.get(entity).remove();
            TransmissionAttachment.get(entity).infect(event.get());
        }
    }
}
