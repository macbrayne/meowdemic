package de.macbrayne.meowdemic.datagen;

import de.macbrayne.meowdemic.Meowdemic;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class MeowdemicEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public static final TagKey<EntityType<?>> DISEASE_SPREADS_TO = TagKey.create(Registries.ENTITY_TYPE, Meowdemic.id("disease_spreads_to"));

    public MeowdemicEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(DISEASE_SPREADS_TO)
                .addOptionalTag(EntityTypeTags.ZOMBIES)
                .add(EntityType.CAT)
                .add(EntityType.PLAYER);
    }
}
