package de.macbrayne.meowdemic;

import de.macbrayne.meowdemic.commands.CommandRoot;
import de.macbrayne.meowdemic.events.MessageEvents;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.world.effects.MeowdemicEffects;
import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import eu.pb4.styledchat.StyledChatEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.SharedConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Meowdemic implements ModInitializer {
	public static final String MOD_ID = "meowdemic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SharedConstants.IS_RUNNING_IN_IDE = true;
		CommandRegistrationCallback.EVENT.register(CommandRoot::register);
		StyledChatEvents.MESSAGE_CONTENT.register(MessageEvents::register);
		Attachments.init();
		MeowdemicEffects.init();
		MeowdemicItems.init();
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
				.register((creativeTab) -> creativeTab.accept(MeowdemicItems.VACCINE));
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
				.register((creativeTab) -> {
					creativeTab.accept(MeowdemicItems.SWAB);
					creativeTab.accept(MeowdemicItems.SWAB_SAMPLE);
				});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}