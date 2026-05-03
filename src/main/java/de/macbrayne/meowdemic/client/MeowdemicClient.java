package de.macbrayne.meowdemic.client;

import com.mojang.blaze3d.platform.InputConstants;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.client.gui.GachaPullScreen;
import de.macbrayne.meowdemic.client.gui.UpgradeGachaScreen;
import de.macbrayne.meowdemic.client.renderer.ModelLayers;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.network.ClientBoundToastRequestPacket;
import de.macbrayne.meowdemic.network.ClientboundGachaResponsePacket;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class MeowdemicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayers.registerModelLayers();

        ClientPlayNetworking.registerGlobalReceiver(ClientboundGachaResponsePacket.TYPE, (payload, context) -> {
            if(context.client().screen instanceof UpgradeGachaScreen gacha) {
                context.client().setScreen(new GachaPullScreen(payload.upgrade()));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ClientBoundToastRequestPacket.TYPE, (payload, context) -> {
            Optional<TransmissionEvent> event = TransmissionAttachment.get(context.player()).getOptional();
            PlayerStatsAttachment.PlayerStatsData playerStats = PlayerStatsAttachment.get(context.player());
            if (event.isPresent()) {
                int nrOfPulls = playerStats.numberOfPullsAffordable();
                context.client().getToastManager().addToast(
                        SystemToast.multiline(context.client(), new SystemToast.SystemToastId(), Component.translatable("gui.meowdemic.upgrades.immunity_rate", nrOfPulls), Component.translatable("gui.meowdemic.upgrades.immunity_rate.description"))
                );
            }
        });

        KeyMapping.Category CATEGORY = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(Meowdemic.MOD_ID, "gacha")
        );

        KeyMapping openScreen = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.meowdemic.open_gacha", // The translation key for the key mapping.
                        InputConstants.Type.KEYSYM, // // The type of the keybinding; KEYSYM for keyboard, MOUSE for mouse.
                        GLFW.GLFW_KEY_J, // The GLFW keycode of the key.
                        CATEGORY // The category of the mapping.
                ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openScreen.consumeClick()) {
                if (client.player != null && TransmissionAttachment.get(client.player).getOptional().isPresent()) {
                    client.setScreen(new UpgradeGachaScreen());
                }
            }
        });
    }
}
