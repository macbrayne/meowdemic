package de.macbrayne.meowdemic.world;

import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.ServerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class SpreadUtil {
    public static void spreadEyeSight(LivingEntity entity, Strain strain) {
        HitResult hitResult = getHitResult(entity.getEyePosition(), entity.getEyePosition().add(entity.getViewVector(1.0F).scale(10 * strain.transmissionFactor())), entity, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE);
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            if (entityHitResult.getEntity() instanceof LivingEntity target && TransmissionAttachment.get(target).tryInfecting(new TransmissionEvent(Optional.of(entity.getUUID()), target.getUUID(), strain))) {
                ServerStatsAttachment.get((ServerLevel) target.level()).addCurrentlyInfected(1);
                PlayerStatsAttachment.get(entity).addEntitiesInfected(1);
                if (!target.getType().equals(entity.getType())) {
                    ServerStatsAttachment.get((ServerLevel) target.level()).addSpeciesBarriersCrossed(1);
                }
            }
        }
    }

    public static void spreadProximity(LivingEntity entity, Strain strain) {
        List<Entity> nearbyEntities = entity.level().getEntities(entity, entity.getBoundingBox().inflate(10 * strain.transmissionFactor()), e -> e instanceof LivingEntity);
        int infectedCount = 0;
        int speciesCount = 0;
        for (Entity nearbyEntity : nearbyEntities) {
            if (nearbyEntity instanceof LivingEntity target && TransmissionAttachment.get(target).tryInfecting(new TransmissionEvent(Optional.of(entity.getUUID()), target.getUUID(), strain))) {
                infectedCount++;
                if (!nearbyEntity.getType().equals(entity.getType())) {
                    speciesCount++;
                }
            }
        }
        ServerStatsAttachment.get((ServerLevel) entity.level()).addCurrentlyInfected(infectedCount);
        ServerStatsAttachment.get((ServerLevel) entity.level()).addSpeciesBarriersCrossed(speciesCount);
        PlayerStatsAttachment.get(entity).addEntitiesInfected(infectedCount);
    }

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
}
