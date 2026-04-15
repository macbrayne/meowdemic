package de.macbrayne.meowdemic.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

public class ModelLayers {
    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(CatEarsModel.LAYER_LOCATION, CatEarsModel::createEarsLayer);
    }
}
