package de.macbrayne.meowdemic.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
@Debug(export = true)
public class WolfMixin {
    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/Wolf;isFood(Lnet/minecraft/world/item/ItemStack;)Z"), cancellable = true)
    public void onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local ItemStack itemStack) {
        if (itemStack.is(MeowdemicItems.SWAB)) {
            itemStack.consume(1, player);
            player.addItem(itemStack.transmuteCopy(MeowdemicItems.SWAB_SAMPLE, 1));
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
