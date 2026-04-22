package de.macbrayne.meowdemic.client;

import com.mojang.blaze3d.platform.InputConstants;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.client.gui.UpgradeGachaScreen;
import de.macbrayne.meowdemic.client.renderer.ModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class MeowdemicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayers.registerModelLayers();

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
                if (client.player != null) {
                    client.setScreen(new UpgradeGachaScreen());
                }
            }
        });
    }
}
