package com.whitewhistle.bleedingedge.datagen;

import com.whitewhistle.bleedingedge.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.List;
import java.util.function.Consumer;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    public void addSmeltingRecipe(Consumer<RecipeJsonProvider> exporter, Item from, Item to, float xp) {
        RecipeProvider.offerSmelting(exporter, List.of(from), RecipeCategory.MISC, to, xp, 200, MOD_ID);
        RecipeProvider.offerBlasting(exporter, List.of(from), RecipeCategory.MISC, to, xp, 100, MOD_ID);
        // TODO: smelter?
    }

    public void addFoodCookingRecipe(Consumer<RecipeJsonProvider> exporter, Item from, Item to, float xp) {
        RecipeProvider.offerSmelting(exporter, List.of(from), RecipeCategory.FOOD, to, xp, 200, MOD_ID);
        RecipeProvider.offerFoodCookingRecipe(exporter, "smoking", RecipeSerializer.SMOKING, 100, from, to, xp);
        RecipeProvider.offerFoodCookingRecipe(exporter, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING, 600, from, to, xp);
    }

    public void addShapelessRecipe(Consumer<RecipeJsonProvider> exporter, RecipeCategory category, List<Object> objects, Item result, int amount) {
        var builder = ShapelessRecipeJsonBuilder.create(category, result, amount);

        for (var object : objects) {
            if (object instanceof Ingredient ingredient) {
                builder.input(ingredient);
                var stacks = ingredient.getMatchingStacks();

                for (var stack : stacks) {
                    var item = stack.getItem();
                    builder.criterion(FabricRecipeProvider.hasItem(item), FabricRecipeProvider.conditionsFromItem(item));
                }
            } else if (object instanceof Item item) {
                builder.input(item);
                builder.criterion(FabricRecipeProvider.hasItem(item), FabricRecipeProvider.conditionsFromItem(item));
            }
        }

        builder.offerTo(exporter);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

        addSmeltingRecipe(exporter, Items.COAL, ModItems.CARBON_INGOT, 0.2f);
        // // smelting
        // addFoodCookingRecipe(exporter, Items.EGG, ModItems.FRIED_EGG, 0.15F);
        // addFoodCookingRecipe(exporter, ModItems.BREAD_SLICE, ModItems.TOAST, 0.1F);
        //
        // // shapeless
        // addShapelessRecipe(exporter, RecipeCategory.FOOD, List.of(Items.BUCKET, Ingredient.ofItems(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM), Items.GLOW_LICHEN, ModItems.CAVE_ONION), ModItems.CAVE_SALAD, 1);
        // addShapelessRecipe(exporter, RecipeCategory.FOOD, List.of(ModItems.OZONE, Items.SUGAR, Items.ENDER_PEARL, Items.STICK), ModItems.SKY_CANDY, 1);
        //
        // // endgame shapeless
        // addShapelessRecipe(exporter, RecipeCategory.FOOD, List.of(ModItems.SKY_CANDY, ModItems.HEARTY_SANDWICH, ModItems.COOKIE_BALL, ModItems.SNIFFER_CENTURY_EGG, ModItems.GLOW_JERKY), ModItems.NATURES_BOUNTY, 1);
        // addShapelessRecipe(exporter, RecipeCategory.FOOD, List.of(Items.NETHER_WART, Items.WITHER_ROSE, Items.BONE, Items.BLAZE_POWDER, Items.COOKED_PORKCHOP, Items.LAVA_BUCKET), ModItems.NETHERS_BOUNTY, 1);
        // addShapelessRecipe(exporter, RecipeCategory.FOOD, List.of(Items.ELYTRA, Items.CHORUS_FRUIT, Items.ENDER_EYE, Items.SPIDER_EYE, Items.INK_SAC, Items.GOLD_NUGGET), ModItems.ENDS_BOUNTY, 1);
        //
        // ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.ESSENCE_OF_SATURATION)
        //         .pattern(" O ")
        //         .pattern("N+E")
        //         .pattern(" S ")
        //         .input('O', ModItems.NATURES_BOUNTY)
        //         .input('N', ModItems.NETHERS_BOUNTY)
        //         .input('E', ModItems.ENDS_BOUNTY)
        //         .input('S', Items.SCULK)
        //         .input('+', Items.NETHER_STAR)
        //         .criterion(FabricRecipeProvider.hasItem(ModItems.NATURES_BOUNTY), FabricRecipeProvider.conditionsFromItem(ModItems.NATURES_BOUNTY))
        //         .criterion(FabricRecipeProvider.hasItem(ModItems.NETHERS_BOUNTY), FabricRecipeProvider.conditionsFromItem(ModItems.NETHERS_BOUNTY))
        //         .criterion(FabricRecipeProvider.hasItem(ModItems.ENDS_BOUNTY), FabricRecipeProvider.conditionsFromItem(ModItems.ENDS_BOUNTY))
        //         .criterion(FabricRecipeProvider.hasItem(Items.SCULK), FabricRecipeProvider.conditionsFromItem(Items.SCULK))
        //         .criterion(FabricRecipeProvider.hasItem(Items.NETHER_STAR), FabricRecipeProvider.conditionsFromItem(Items.NETHER_STAR))
        //         .offerTo(exporter);
        //
        // ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.COOKIE_BALL)
        //         .pattern(" C ")
        //         .pattern("CCC")
        //         .pattern(" C ")
        //         .input('C', Items.COOKIE)
        //         .criterion(FabricRecipeProvider.hasItem(Items.COOKIE), FabricRecipeProvider.conditionsFromItem(Items.COOKIE))
        //         .offerTo(exporter);
        //
        // ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.CLEAVER)
        //         .pattern("II ")
        //         .pattern("IA ")
        //         .pattern(" S ")
        //         .input('I', Items.IRON_INGOT)
        //         .input('A', Items.IRON_AXE)
        //         .input('S', Items.STICK)
        //         .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
        //         .criterion(FabricRecipeProvider.hasItem(Items.STICK), FabricRecipeProvider.conditionsFromItem(Items.STICK))
        //         .offerTo(exporter);
        //
        // ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.PRESERVES_JAR)
        //         .pattern("B")
        //         .pattern("P")
        //         .input('P', Items.FLOWER_POT)
        //         .input('B', Items.BRICK)
        //         .criterion(FabricRecipeProvider.hasItem(Items.BRICK), FabricRecipeProvider.conditionsFromItem(Items.BRICK))
        //         .offerTo(exporter);
    }
}