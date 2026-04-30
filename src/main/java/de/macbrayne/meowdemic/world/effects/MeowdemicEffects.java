package de.macbrayne.meowdemic.world.effects;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.world.attachments.entity.ImmunityAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.IncubationAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import net.fabricmc.fabric.api.entity.event.v1.effect.ServerMobEffectEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class MeowdemicEffects {
    public static final Holder<MobEffect> INFECTED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Meowdemic.id("infected"), new InfectionEffect());
    public static final Holder<MobEffect> IMMUNE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Meowdemic.id("immune"), new ImmunityEffect());
    public static final Holder<MobEffect> INCUBATING = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Meowdemic.id("incubating"), new IncubationEffect());

    public static void init() {
        ServerMobEffectEvents.ALLOW_EARLY_REMOVE.register((instance, entity, ctx) -> {
            if (IncubationAttachment.get(entity).getOptional().isPresent() && instance.getEffect() == INCUBATING)
                return false;
            if (TransmissionAttachment.get(entity).getOptional().isPresent() && instance.getEffect() == INFECTED)
                return false;
            if (ImmunityAttachment.get(entity).getOptional().isPresent() && instance.getEffect() == IMMUNE)
                return false;
            return true;
        });

        ServerMobEffectEvents.AFTER_REMOVE.register((instance, entity, ctx) -> {
            if(instance.getEffect() == INCUBATING) {
                IncubationEffect.onRemove(instance, entity);
            }
            if(instance.getEffect() == INFECTED) {
                InfectionEffect.onRemove(instance, entity);
            }
            if(instance.getEffect() == IMMUNE) {
                ImmunityEffect.onRemove(instance, entity);
            }
        });
    }
}
