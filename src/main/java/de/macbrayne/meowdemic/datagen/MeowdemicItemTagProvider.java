package de.macbrayne.meowdemic.datagen;

import de.macbrayne.meowdemic.Meowdemic;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class MeowdemicItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public static final TagKey<Item> INFECTABLE = TagKey.create(Registries.ITEM, Meowdemic.id("infectable"));

    public MeowdemicItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(INFECTABLE)
                .addOptionalTag(ConventionalItemTags.FOODS)
                .addOptionalTag(ConventionalItemTags.DRINKS);
    }
}
