package de.macbrayne.meowdemic.mixin;

import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.SpreadUtil;
import de.macbrayne.meowdemic.world.attachments.ServerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import de.macbrayne.meowdemic.world.item.components.AffectionConsumeEffect;
import de.macbrayne.meowdemic.world.item.components.StrainTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.feline.CatSoundVariants;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, WaypointTransmitter {
    @Shadow
    public abstract boolean isAlive();

    @Unique
    private int meowdemic$spreadTime;

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Unique
    private void meowdemic$resetSpreadTime() {
        meowdemic$spreadTime = -80;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void meowdemic$spread(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Optional<TransmissionEvent> event = TransmissionAttachment.get(entity).getOptional();
        if (event.isEmpty() || !this.isAlive()) return;

        Strain strain = event.get().strain();
        HashSet<Symptoms> symptoms = strain.symptoms();
        float modifier = Mth.sqrt(symptoms.size());

        this.meowdemic$spreadTime++;
        if (symptoms.contains(Symptoms.MEOW_AND_PURR) && this.random.nextInt((int) (500 * modifier)) <= this.meowdemic$spreadTime) {
            meowdemic$resetSpreadTime();
            // Spread to nearby entities
            System.out.println("Attempting to spread from " + entity.getName().getString());
            if (!entity.level().isClientSide()) {
                SpreadUtil.spreadEyeSight(entity, strain);
            }
            this.playSound(SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds().ambientSound().value());
        }

        if (symptoms.contains(Symptoms.MEOW_AND_PURR) && this.random.nextInt((int) (500 * modifier)) <= this.meowdemic$spreadTime) {
            meowdemic$resetSpreadTime();

            System.out.println("Attempting to spread radius from " + entity.getName().getString());
            if (!entity.level().isClientSide()) {
                SpreadUtil.spreadProximity(entity, strain);
            }
            this.playSound(SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds().purrSound().value());
        }

        if (symptoms.contains(Symptoms.FOOD) && this.random.nextInt((int) (500 * modifier)) <= this.meowdemic$spreadTime) {
            System.out.println("Attempting to infect food from " + entity.getName().getString());
            if(!level().isClientSide() && entity.getMainHandItem().has(DataComponents.CONSUMABLE)) {
                Consumable oldConsumable = entity.getMainHandItem().get(DataComponents.CONSUMABLE);
                Consumable consumable = MeowdemicItems.modifyConsumableEffect(oldConsumable, AffectionConsumeEffect.infect(Optional.of(this.getUUID()), strain));
                if (consumable == null) return;
                entity.getMainHandItem().set(DataComponents.CONSUMABLE, consumable);
                entity.getMainHandItem().set(MeowdemicItems.STRAIN_TOOLTIP, new StrainTooltipComponent());
                meowdemic$resetSpreadTime();
            }
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    public void meowdemic$onDeath(RemovalReason reason, CallbackInfo ci) {
        if ((reason == RemovalReason.DISCARDED || reason == RemovalReason.KILLED) && TransmissionAttachment.get((LivingEntity) (Object) this).getOptional().isPresent()) {
            ServerStatsAttachment.get((ServerLevel) this.level()).removeCurrentlyInfected();
        }
    }
}
