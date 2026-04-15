package de.macbrayne.meowdemic.client;

import de.macbrayne.meowdemic.client.renderer.ModelLayers;
import net.fabricmc.api.ClientModInitializer;

public class MeowdemicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayers.registerModelLayers();
    }
}
