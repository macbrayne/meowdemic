package de.macbrayne.meowdemic.mixin;

import de.macbrayne.meowdemic.attachments.entity.TransmissionComponent;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.minecraft.sounds.SoundEvent;
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
import org.jspecify.annotations.Nullable;
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
    public abstract void makeSound(@Nullable SoundEvent sound);

    @Shadow
    public abstract boolean isAlive();

    @Unique
    private int spreadTime;

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Unique
    private static HitResult getHitResult(Vec3 from, Vec3 to, Entity entity, ClipContext.Block blockContext, ClipContext.Fluid fluidContext) {
        HitResult hitResult = entity.level().clip(new ClipContext(from, to, blockContext, fluidContext, entity));
        if (hitResult.getType() != HitResult.Type.MISS) {
            to = hitResult.getLocation();
        }
        HitResult entityHitResult = ProjectileUtil.getEntityHitResult(entity.level(), entity, from, to, entity.getBoundingBox().expandTowards(entity.getDeltaMovement()).inflate(1), e -> !e.isSpectator(), 1f);
        if (entityHitResult != null) {
            hitResult = entityHitResult;
        }
        return hitResult;
    }

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
        if (symptoms.contains(Symptoms.MEOWING) && this.random.nextInt((int) (500 * modifier)) <= this.spreadTime) {
            resetSpreadTime();
            // Spread to nearby entities
            System.out.println("Attempting to spread from " + entity.getName().getString());
            if (!entity.level().isClientSide()) {
                HitResult hitResult = getHitResult(entity.getEyePosition(), entity.getEyePosition().add(entity.getViewVector(1.0F).scale(10 * strain.transmissionFactor())), entity, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE);
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                    if (entityHitResult.getEntity() instanceof LivingEntity target) {
                        TransmissionComponent.get(target).setIfNone(new TransmissionEvent(Optional.of(entity.getUUID()), target.getUUID(), event.get().strain().mutate()));
                    }
                }
            }
            this.playSound(SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds().ambientSound().value());
        }

        if (symptoms.contains(Symptoms.PURRING) && this.random.nextInt((int) (500 * modifier)) <= this.spreadTime) {
            resetSpreadTime();

            System.out.println("Attempting to spread radius from " + entity.getName().getString());
            if (!entity.level().isClientSide()) {
                List<Entity> nearbyEntities = entity.level().getEntities(entity, entity.getBoundingBox().inflate(10 * strain.transmissionFactor()), e -> e instanceof LivingEntity);
                for (Entity nearbyEntity : nearbyEntities) {
                    if (nearbyEntity instanceof LivingEntity target) {
                        TransmissionComponent.get(target).setIfNone(new TransmissionEvent(Optional.of(entity.getUUID()), target.getUUID(), event.get().strain().mutate()));
                    }
                }
            }
            this.playSound(SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds().purrSound().value());
        }
    }
}
