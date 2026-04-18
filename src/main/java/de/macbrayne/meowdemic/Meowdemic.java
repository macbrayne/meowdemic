package de.macbrayne.meowdemic;

import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.commands.CommandRoot;
import de.macbrayne.meowdemic.world.effects.ImmunityEffect;
import de.macbrayne.meowdemic.world.effects.InfectionEffect;
import de.macbrayne.meowdemic.events.MessageEvents;
import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import eu.pb4.styledchat.StyledChatEvents;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Meowdemic implements ModInitializer {
	public static final String MOD_ID = "meowdemic";
	public static final Holder<MobEffect> INFECTED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id("infected"), new InfectionEffect());
	public static final Holder<MobEffect> IMMUNE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id("immune"), new ImmunityEffect());

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SharedConstants.IS_RUNNING_IN_IDE = true;
		CommandRegistrationCallback.EVENT.register(CommandRoot::register);
		StyledChatEvents.MESSAGE_CONTENT.register(MessageEvents::register);
		Attachments.init();
		MeowdemicItems.init();
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
				.register((creativeTab) -> creativeTab.accept(MeowdemicItems.VACCINE));

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}