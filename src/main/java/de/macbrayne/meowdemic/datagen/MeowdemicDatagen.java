package de.macbrayne.meowdemic.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MeowdemicDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(MeowdemicItemTagProvider::new);
        pack.addProvider(MeowdemicEntityTypeTagProvider::new);
        pack.addProvider(MeowdemicRecipeProvider::new);
    }
}
