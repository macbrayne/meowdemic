package de.macbrayne.meowdemic;

import de.macbrayne.meowdemic.commands.CommandRoot;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.data.Upgrades;
import de.macbrayne.meowdemic.events.MessageEvents;
import de.macbrayne.meowdemic.network.ClientboundGachaResponsePacket;
import de.macbrayne.meowdemic.network.ServerboundGachaRequestPacket;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import de.macbrayne.meowdemic.world.effects.MeowdemicEffects;
import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import eu.pb4.styledchat.StyledChatEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.SharedConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

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
		PayloadTypeRegistry.clientboundPlay().register(ClientboundGachaResponsePacket.TYPE, ClientboundGachaResponsePacket.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(ServerboundGachaRequestPacket.TYPE, ServerboundGachaRequestPacket.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(ServerboundGachaRequestPacket.TYPE, (payload, context) -> {
			RandomSource random = context.player().getRandom();
            Optional<TransmissionEvent> event = TransmissionAttachment.get(context.player()).getOptional();
			if(event.isPresent() && PlayerStatsAttachment.get(context.player()).getPoints() > 0) {
				int pity = PlayerStatsAttachment.get(context.player()).getGachaPity();
				Upgrades upgrade = Upgrades.getRandom(random, pity);
				context.responseSender().sendPacket(new ClientboundGachaResponsePacket(upgrade));
				PlayerStatsAttachment.get(context.player()).removePoints(1);
				PlayerStatsAttachment.get(context.player()).increaseGachaPity();
				if(pity >= 10) {
					PlayerStatsAttachment.get(context.player()).resetGachaPity();
				}
			}
		});

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}