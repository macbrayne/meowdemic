package de.macbrayne.meowdemic.world.item;

import com.mojang.serialization.MapCodec;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.world.item.components.AffectionConsumeEffect;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.function.Function;

public class MeowdemicItems {
    public static final ConsumeEffect.Type<AffectionConsumeEffect> AFFECT = registerConsumeEffect(
            "affect", AffectionConsumeEffect.CODEC, AffectionConsumeEffect.STREAM_CODEC
    );

    public static final Consumable VACCINE_CONSUMABLE = Consumables.defaultDrink()
            .consumeSeconds(5.0F)
            .sound(SoundEvents.HONEY_DRINK)
            .build();

    public static final Item VACCINE = registerItem("vaccine", Item::new, new Item.Properties()
            .component(DataComponents.CONSUMABLE, VACCINE_CONSUMABLE));
    public static final Item SWAB = registerItem("swab", Item::new, new Item.Properties().component(DataComponents.MAX_STACK_SIZE, 16));
    public static final Item SWAB_SAMPLE = registerItem("swab_sample", Item::new, new Item.Properties().component(DataComponents.MAX_STACK_SIZE, 16));

    public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Meowdemic.id(name));

        // Create the item instance.
        T item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((creativeTab) -> creativeTab.accept(MeowdemicItems.VACCINE));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((creativeTab) -> {
                    creativeTab.accept(MeowdemicItems.SWAB);
                    creativeTab.accept(MeowdemicItems.SWAB_SAMPLE);
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register((creativeTab) -> {
                    ItemStack defaultVaccine = VACCINE.getDefaultInstance();
                    Consumable consumable = addEffect(VACCINE_CONSUMABLE, AffectionConsumeEffect.vaccinate(new Strain(Symptoms.all(), 1, 1, 1, 1)));
                    defaultVaccine.set(DataComponents.CONSUMABLE, consumable);
                    creativeTab.accept(defaultVaccine);
                });
    }

    private static <T extends ConsumeEffect> ConsumeEffect.Type<T> registerConsumeEffect(
            final String name, final MapCodec<T> codec, final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec
    ) {
        return Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, name, new ConsumeEffect.Type<>(codec, streamCodec));
    }

    public static Consumable addEffect(Consumable oldConsumable, AffectionConsumeEffect effect) {
        Consumable.Builder builder = Consumable.builder()
                .consumeSeconds(oldConsumable.consumeSeconds())
                .animation(oldConsumable.animation())
                .hasConsumeParticles(oldConsumable.hasConsumeParticles())
                .sound(oldConsumable.sound());
        for (ConsumeEffect onConsumeEffect : oldConsumable.onConsumeEffects()) {
            builder.onConsume(onConsumeEffect);
            if(onConsumeEffect instanceof AffectionConsumeEffect) {
                return null;
            }
        }
        builder.onConsume(effect);
        return builder.build();
    }
}
