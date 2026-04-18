package de.macbrayne.meowdemic.world.item;

import com.mojang.serialization.MapCodec;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.world.item.components.AffectionConsumeEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.Optional;
import java.util.function.Function;

public class MeowdemicItems {
    public static final ConsumeEffect.Type<ApplyStatusEffectsConsumeEffect> AFFECT = registerConsumeEffect(
            "affect", ApplyStatusEffectsConsumeEffect.CODEC, ApplyStatusEffectsConsumeEffect.STREAM_CODEC
    );

    public static final Consumable VACCINE_CONSUMABLE = Consumables.defaultDrink()
            .consumeSeconds(5.0F)
            .sound(SoundEvents.HONEY_DRINK)
            .onConsume(new AffectionConsumeEffect(Optional.empty(), new Strain("", Symptoms.all(), 1, 1, 1), true, 1))
            .build();

    public static final Item VACCINE = registerItem("vaccine", Item::new, new Item.Properties()
            .component(DataComponents.CONSUMABLE, VACCINE_CONSUMABLE));

    public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Meowdemic.MOD_ID, name));

        // Create the item instance.
        T item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void init() {

    }

    private static <T extends ConsumeEffect> ConsumeEffect.Type<T> registerConsumeEffect(
            final String name, final MapCodec<T> codec, final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec
    ) {
        return Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, name, new ConsumeEffect.Type<>(codec, streamCodec));
    }
}
