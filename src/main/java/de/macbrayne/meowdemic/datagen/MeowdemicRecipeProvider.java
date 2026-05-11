package de.macbrayne.meowdemic.datagen;

import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class MeowdemicRecipeProvider extends FabricRecipeProvider {
    public MeowdemicRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
                Ingredient foodAndDrinks = Ingredient.of(itemLookup.getOrThrow(MeowdemicItemTagProvider.INFECTABLE));

                shaped(RecipeCategory.MISC, MeowdemicItems.SWAB, 16)
                        .pattern("  w")
                        .pattern(" s ")
                        .pattern("w  ")
                        .define('w', ItemTags.WOOL)
                        .define('s', Items.STICK)
                        .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                        .unlockedBy(getHasName(Items.WHITE_WOOL), has(ItemTags.WOOL))
                        .save(exporter);

                shaped(RecipeCategory.MISC, MeowdemicItems.VACCINE, 4)
                        .pattern(" c ")
                        .pattern("isi")
                        .pattern(" n ")
                        .define('n', Items.IRON_NUGGET)
                        .define('i', Items.IRON_INGOT)
                        .define('s', MeowdemicItems.SWAB_SAMPLE)
                        .define('c', foodAndDrinks)
                        .unlockedBy(getHasName(MeowdemicItems.SWAB), has(MeowdemicItems.SWAB))
                        .save(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "MeowdemicRecipeProvider";
    }
}
