package de.macbrayne.meowdemic.world.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.awt.*;
import java.util.function.Consumer;

public record StrainTooltipComponent() implements TooltipProvider {

    public static final Codec<StrainTooltipComponent> CODEC = MapCodec.of(Encoder.empty(), Decoder.unit(StrainTooltipComponent::new)).codec();

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if(components.get(DataComponents.CONSUMABLE) == null) return;
        for(ConsumeEffect effect : components.get(DataComponents.CONSUMABLE).onConsumeEffects()) {
            if(effect.getType() == MeowdemicItems.AFFECT) {
                AffectionConsumeEffect affectionEffect = (AffectionConsumeEffect) effect;
                if(affectionEffect.vaccinate()) {
                    consumer.accept(Component.translatable("tooltip.meowdemic.vaccine_strain", affectionEffect.strain().name()).withColor(Color.PINK.getRGB()));
                    return;
                }
                consumer.accept(Component.translatable("tooltip.meowdemic.strain").withColor(Color.PINK.getRGB()));
                return;
            }
        }
    }
}
