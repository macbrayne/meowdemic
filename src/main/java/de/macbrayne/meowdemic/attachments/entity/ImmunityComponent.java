package de.macbrayne.meowdemic.attachments.entity;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Strain;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class ImmunityComponent {
    public static final AttachmentType<Strain> TYPE = AttachmentRegistry.create(Meowdemic.id("immunity"), builder -> builder
            .persistent(Strain.CODEC));

    public static ImmunityData get(LivingEntity target) {
        return new ImmunityData(target);
    }

    public record ImmunityData(LivingEntity target) {
        public Optional<Strain> getOptional() {
            return Optional.ofNullable(target.getAttached(TYPE));
        }

        public void setIfNone(Strain strain) {
            if(target.getAttached(TYPE) != null) return;

            MobEffectInstance effect = new MobEffectInstance(Meowdemic.IMMUNE,
                    (int)(60 * 20 * strain.immunityFactor()),
                    0, false, false, false);
            target.addEffect(effect);
            target.setAttached(TYPE, strain);
        }
    }
}
