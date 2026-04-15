package de.macbrayne.meowdemic.effects;

import de.macbrayne.meowdemic.attachments.Attachments;
import de.macbrayne.meowdemic.attachments.entity.ImmunityComponent;
import de.macbrayne.meowdemic.attachments.entity.ServerStatsComponent;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
        TransmissionEvent event = entity.getAttached(Attachments.TRANSMISSION);
        if(event != null && !entity.level().isClientSide()) {
            entity.removeAttached(Attachments.TRANSMISSION);
            ServerStatsComponent.get((ServerLevel) entity.level()).removeCurrentlyInfected();
            ImmunityComponent.get(entity).setIfNone(event.strain());
        }

        super.onEffectRemoved(effectInstance, entity);
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }
}
