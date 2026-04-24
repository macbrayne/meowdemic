package de.macbrayne.meowdemic.world.effects;

import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.world.attachments.ServerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.ImmunityAttachment;
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

    public static void onRemove(MobEffectInstance effectInstance, LivingEntity entity) {
        TransmissionEvent event = entity.getAttached(Attachments.TRANSMISSION);
        if(event != null && !entity.level().isClientSide()) {
            entity.removeAttached(Attachments.TRANSMISSION);
            ServerStatsAttachment.get((ServerLevel) entity.level()).removeCurrentlyInfected();
            ImmunityAttachment.get(entity).setIfNone(event.strain(), 1f);
        }
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }
}
