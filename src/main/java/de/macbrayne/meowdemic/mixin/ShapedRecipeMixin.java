package de.macbrayne.meowdemic.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import de.macbrayne.meowdemic.world.item.components.AffectionConsumeEffect;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
    @WrapOperation(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStackTemplate;create()Lnet/minecraft/world/item/ItemStack;"))
    ItemStack injectVaccineRecipe(ItemStackTemplate instance, Operation<ItemStack> original, @Local(argsOnly = true) final CraftingInput input) {
        ItemStack result = original.call(instance);

        AffectionConsumeEffect affected = null;
        for(ItemStack item : input.items()) {
            if(item.has(DataComponents.CONSUMABLE)) {
                for(ConsumeEffect effect : item.get(DataComponents.CONSUMABLE).onConsumeEffects()) {
                    if(effect.getType() == MeowdemicItems.AFFECT) {
                        affected = (AffectionConsumeEffect) effect;
                        System.out.println("Found affection effect in crafting input: " + affected);
                        break;
                    }
                }
            }
        }
        if(affected == null) {
            return result;
        }
        if(result.is(MeowdemicItems.VACCINE)) {
            affected = AffectionConsumeEffect.vaccinate(affected.strain());
        }

        System.out.println("Post modify affect: " + affected);
        if (result.has(DataComponents.CONSUMABLE)) {
            Consumable old = result.get(DataComponents.CONSUMABLE);
            Consumable newConsumable = MeowdemicItems.addConsumableEffect(old, affected);
            if(newConsumable == null) {
                return result;
            }
            result.set(DataComponents.CONSUMABLE, newConsumable);
        }

        System.out.println("Result: " + result.getComponents());
        return result;
    }
}
