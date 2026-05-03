package de.macbrayne.meowdemic.mixin;

import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.entity.IncubationAttachment;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal {
    @Unique
    private final RandomSource randomSource = RandomSource.create();
    protected CatMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }


    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectDisease(EntityType<?> type, Level level, CallbackInfo ci) {
        if(!level.isClientSide() && random.nextFloat() < 0.1f) {
            IncubationAttachment.get(this).forceIncubate(new TransmissionEvent(Optional.empty(), this.getUUID(), Strain.random(randomSource)));
        }
    }
}
