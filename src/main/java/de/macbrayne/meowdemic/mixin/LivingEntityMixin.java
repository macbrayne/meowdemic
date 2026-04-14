package de.macbrayne.meowdemic.mixin;

import de.macbrayne.meowdemic.attachments.entity.TransmissionComponent;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.util.SpreadUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.feline.CatSoundVariants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, WaypointTransmitter {
    @Shadow
    public abstract boolean isAlive();

    @Unique
    private int spreadTime;

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Unique
    private void resetSpreadTime() {
        spreadTime = -80;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void spread(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Optional<TransmissionEvent> event = TransmissionComponent.get(entity).getOptional();
        if (entity instanceof Player) {
            System.out.println("Is present: " + event.isPresent() + " is on client: + " + entity.level().isClientSide());
        }
        if (event.isEmpty() || !this.isAlive()) return;

        Strain strain = event.get().strain();
        List<Symptoms> symptoms = strain.symptoms();
        float modifier = Mth.sqrt(symptoms.size());

        this.spreadTime++;
        if (symptoms.contains(Symptoms.MEOW_AND_PURR) && this.random.nextInt((int) (500 * modifier)) <= this.spreadTime) {
            resetSpreadTime();
            // Spread to nearby entities
            System.out.println("Attempting to spread from " + entity.getName().getString());
            if (!entity.level().isClientSide()) {
                SpreadUtil.spreadEyeSight(entity, strain.mutate());
            }
            this.playSound(SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds().ambientSound().value());
        }

        if (symptoms.contains(Symptoms.MEOW_AND_PURR) && this.random.nextInt((int) (500 * modifier)) <= this.spreadTime) {
            resetSpreadTime();

            System.out.println("Attempting to spread radius from " + entity.getName().getString());
            if (!entity.level().isClientSide()) {
                SpreadUtil.spreadProximity(entity, strain.mutate());
            }
            this.playSound(SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds().purrSound().value());
        }
    }
}
