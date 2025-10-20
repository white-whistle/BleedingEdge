package com.whitewhistle.bleedingedge.datagen;

import com.google.gson.JsonObject;
import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.recipe.AssemblerRecipe;
import com.whitewhistle.bleedingedge.recipe.ModRecipeTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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

        // ===== vanilla smelting =====
        addSmeltingRecipe(exporter, Items.COAL, ModItems.CARBON_INGOT, 0.2f);


        // ===== vanilla crafting =====
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BASIC_SLOT)
                .pattern("ICI")
                .pattern("CTC")
                .pattern("ICI")
                .input('C', ModItems.CARBON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('I', Items.IRON_INGOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BRAIN_JAR)
                .pattern("GGG")
                .pattern("G G")
                .pattern("CCC")
                .input('C', ModItems.CARBON_INGOT)
                .input('G', Items.GLASS_PANE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.GLASS_PANE), FabricRecipeProvider.conditionsFromItem(Items.GLASS_PANE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BRAIN_DRILL)
                .pattern("CRC")
                .pattern("ICI")
                .pattern(" I ")
                .input('C', ModItems.CARBON_INGOT)
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ASSEMBLY_SLOT)
                .pattern("CTC")
                .pattern("TST")
                .pattern("CTC")
                .input('C', ModItems.CARBON_INGOT)
                .input('S', ModItems.BASIC_SLOT)
                .input('T', Items.CRAFTING_TABLE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(Items.CRAFTING_TABLE), FabricRecipeProvider.conditionsFromItem(Items.CRAFTING_TABLE))
                .offerTo(exporter);

        // ===== assembler recipes =====
        // [2x2]
        // TODO: make this generic food item
        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.NUTRIENT_BLOCK, 16)
                .pattern("FF")
                .pattern("FF")
                .input('F', Items.WHEAT)
                .criterion(FabricRecipeProvider.hasItem(Items.WHEAT), FabricRecipeProvider.conditionsFromItem(Items.WHEAT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);


        // [3x3]
        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TECHNOLOGY)
                .pattern("RIR")
                .pattern("CEC")
                .pattern("RIR")
                .input('C', ModItems.CARBON_INGOT)
                .input('E', ModItems.EUREKA)
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.EUREKA), FabricRecipeProvider.conditionsFromItem(ModItems.EUREKA))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.HEATING_SLOT)
                .pattern("LBL")
                .pattern("BSB")
                .pattern("LBL")
                .input('S', ModItems.BASIC_SLOT)
                .input('L', Items.LAVA_BUCKET)
                .input('B', Items.IRON_BARS)
                .criterion(FabricRecipeProvider.hasItem(Items.LAVA_BUCKET), FabricRecipeProvider.conditionsFromItem(Items.LAVA_BUCKET))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.REPAIR_SLOT)
                .pattern("TCT")
                .pattern("CSC")
                .pattern("TAT")
                .input('S', ModItems.BASIC_SLOT)
                .input('C', ModItems.CARBON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('A', Items.ANVIL)
                .criterion(FabricRecipeProvider.hasItem(Items.ANVIL), FabricRecipeProvider.conditionsFromItem(Items.ANVIL))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        // [3x4]
        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BREACH_HAMMER)
                .pattern("IPT")
                .pattern("ICR")
                .pattern(" C ")
                .pattern(" C ")
                .input('C', ModItems.CARBON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('P', Items.STICKY_PISTON)
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                .criterion(FabricRecipeProvider.hasItem(Items.STICKY_PISTON), FabricRecipeProvider.conditionsFromItem(Items.STICKY_PISTON))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        // [4x4]
        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.OBSIDIAN_SASH)
                .pattern("OOOO")
                .pattern("O  O")
                .pattern("O  O")
                .pattern("OTCO")
                .input('T', ModItems.TECHNOLOGY)
                .input('O', Items.OBSIDIAN)
                .input('C', Items.CRYING_OBSIDIAN)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(Items.OBSIDIAN), FabricRecipeProvider.conditionsFromItem(Items.OBSIDIAN))
                .criterion(FabricRecipeProvider.hasItem(Items.CRYING_OBSIDIAN), FabricRecipeProvider.conditionsFromItem(Items.CRYING_OBSIDIAN))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.STEEL_KIDNEY)
                .pattern(" CC ")
                .pattern("CGGC")
                .pattern("CSPC")
                .pattern(" TT ")
                .input('C', ModItems.CARBON_INGOT)
                .input('G', Items.GREEN_STAINED_GLASS_PANE)
                .input('T', ModItems.TECHNOLOGY)
                .input('S', Items.FERMENTED_SPIDER_EYE)
                .input('P', Items.POISONOUS_POTATO)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.GLASS), FabricRecipeProvider.conditionsFromItem(Items.GLASS))
                .criterion(FabricRecipeProvider.hasItem(Items.FERMENTED_SPIDER_EYE), FabricRecipeProvider.conditionsFromItem(Items.FERMENTED_SPIDER_EYE))
                .criterion(FabricRecipeProvider.hasItem(Items.POISONOUS_POTATO), FabricRecipeProvider.conditionsFromItem(Items.POISONOUS_POTATO))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.LIQUID_COOLING)
                .pattern("CCCC")
                .pattern("TGPG")
                .pattern("TGSG")
                .pattern(" GGG")
                .input('C', ModItems.CARBON_INGOT)
                .input('G', Items.GLASS_PANE)
                .input('T', ModItems.TECHNOLOGY)
                .input('P', Items.CARVED_PUMPKIN)
                .input('S', Items.SNOW_BLOCK)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BRAIN), FabricRecipeProvider.conditionsFromItem(ModItems.BRAIN))
                .criterion(FabricRecipeProvider.hasItem(Items.GLASS), FabricRecipeProvider.conditionsFromItem(Items.GLASS))
                .criterion(FabricRecipeProvider.hasItem(Items.SNOW_BLOCK), FabricRecipeProvider.conditionsFromItem(Items.SNOW_BLOCK))
                .criterion(FabricRecipeProvider.hasItem(Items.CARVED_PUMPKIN), FabricRecipeProvider.conditionsFromItem(Items.CARVED_PUMPKIN))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.THREAT_VISOR)
                .pattern("C  C")
                .pattern("C  C")
                .pattern("TCCC")
                .pattern("GG  ")
                .input('C', ModItems.CARBON_INGOT)
                .input('G', Items.RED_STAINED_GLASS_PANE)
                .input('T', ModItems.TECHNOLOGY)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.RED_STAINED_GLASS_PANE), FabricRecipeProvider.conditionsFromItem(Items.RED_STAINED_GLASS_PANE))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.NIGHT_VISION_GOGGLES)
                .pattern("C  C")
                .pattern("CNNC")
                .pattern("GTTG")
                .pattern("CGGC")
                .input('C', ModItems.CARBON_INGOT)
                .input('G', Items.LIME_STAINED_GLASS_PANE)
                .input('T', ModItems.TECHNOLOGY)
                .input('N', Items.DAYLIGHT_DETECTOR)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.GLASS), FabricRecipeProvider.conditionsFromItem(Items.GLASS))
                .criterion(FabricRecipeProvider.hasItem(Items.DAYLIGHT_DETECTOR), FabricRecipeProvider.conditionsFromItem(Items.DAYLIGHT_DETECTOR))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        // [4x5]

        // [5x5]
        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.STEEL_BRAIN)
                .pattern(" CCC ")
                .pattern("CBTBC")
                .pattern("CBTBC")
                .pattern("CCTCC")
                .pattern("  T  ")
                .input('C', ModItems.CARBON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('B', ModItems.BRAIN)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BRAIN), FabricRecipeProvider.conditionsFromItem(ModItems.BRAIN))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.KEVLAR_TOTEM)
                .pattern(" CCC ")
                .pattern(" GbG ")
                .pattern("ICTCI")
                .pattern(" CgC ")
                .pattern(" CCC ")
                .input('C', ModItems.CARBON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('b', ModItems.SMALL_BRAIN)
                .input('I', Items.IRON_INGOT)
                .input('g', Items.GOLDEN_APPLE)
                .input('G', Items.LIME_STAINED_GLASS_PANE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.SMALL_BRAIN), FabricRecipeProvider.conditionsFromItem(ModItems.SMALL_BRAIN))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.GOLDEN_APPLE), FabricRecipeProvider.conditionsFromItem(Items.GOLDEN_APPLE))
                .criterion(FabricRecipeProvider.hasItem(Items.LIME_STAINED_GLASS_PANE), FabricRecipeProvider.conditionsFromItem(Items.LIME_STAINED_GLASS_PANE))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SHIELD_GENERATOR)
                .pattern(" IC  ")
                .pattern("AICCC")
                .pattern("TTCGC")
                .pattern("TTCCC")
                .pattern(" IC  ")
                .input('C', ModItems.CARBON_INGOT)
                .input('I', Items.IRON_INGOT)
                .input('G', Items.BLUE_STAINED_GLASS_PANE)
                .input('A', Items.AMETHYST_SHARD)
                .input('T', ModItems.TECHNOLOGY)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD), FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(FabricRecipeProvider.hasItem(Items.GLASS), FabricRecipeProvider.conditionsFromItem(Items.GLASS))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.HOVER_PACK)
                .pattern(" I I ")
                .pattern("ITCTI")
                .pattern(" CHC ")
                .pattern("ITCTI")
                .pattern(" I I ")
                .input('C', ModItems.CARBON_INGOT)
                .input('I', Items.IRON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('H', ModItems.HEATING_SLOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.HEATING_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.HEATING_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.STORM_BENDER)
                .pattern(" CCCC")
                .pattern("CITRL")
                .pattern("CTR  ")
                .pattern("CR   ")
                .pattern("C    ")
                .input('C', ModItems.CARBON_INGOT)
                .input('I', Items.IRON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('R', Items.REDSTONE)
                .input('L', Items.LIGHTNING_ROD)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.LIGHTNING_ROD), FabricRecipeProvider.conditionsFromItem(Items.LIGHTNING_ROD))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.CLOAKING_DEVICE)
                .pattern("IIIII")
                .pattern("I   I")
                .pattern("ICCCI")
                .pattern("ICTEC")
                .pattern("CCTEC")
                .input('C', ModItems.CARBON_INGOT)
                .input('I', Items.IRON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('E', Items.ENDER_EYE)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.ENDER_EYE), FabricRecipeProvider.conditionsFromItem(Items.ENDER_EYE))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.TESLA_PACK)
                .pattern(" ccc ")
                .pattern("cTCTc")
                .pattern("cCSCc")
                .pattern("cTCTc")
                .pattern(" ccc ")
                .input('C', ModItems.CARBON_INGOT)
                .input('c', Items.COPPER_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('S', ModItems.BASIC_SLOT)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.COPPER_INGOT), FabricRecipeProvider.conditionsFromItem(Items.COPPER_INGOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .offerTo(exporter);

        ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.TACTICORE)
                .pattern("CCCCC")
                .pattern("CTTTC")
                .pattern("CT+TC")
                .pattern("CTTTC")
                .pattern("CCCCC")
                .input('C', ModItems.CARBON_INGOT)
                .input('T', ModItems.TECHNOLOGY)
                .input('+', Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                .criterion(FabricRecipeProvider.hasItem(Items.NETHER_STAR), FabricRecipeProvider.conditionsFromItem(Items.NETHER_STAR))
                .offerTo(exporter);

        List.of(
                new UpgradeCoreRecipeEntry(ModItems.SPIDERCORE, Items.FERMENTED_SPIDER_EYE, Items.SPIDER_EYE, Items.EMERALD),
                new UpgradeCoreRecipeEntry(ModItems.HEAVYCORE, Items.NETHERITE_INGOT, Items.NETHERITE_SCRAP, Items.DIAMOND),
                new UpgradeCoreRecipeEntry(ModItems.MEDICORE, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE, Items.QUARTZ)
        ).forEach(e -> {

            ShapedAssemblyRecipeJsonBuilder.create(RecipeCategory.COMBAT, e.result)
                    .pattern("TTETT")
                    .pattern("TSFST")
                    .pattern("EF@FE")
                    .pattern("TSFST")
                    .pattern("TTETT")
                    .input('F', e.ring1)
                    .input('S', e.ring2)
                    .input('E', e.ring3)
                    .input('T', ModItems.TECHNOLOGY)
                    .input('@', ModItems.TACTICORE)
                    .criterion(FabricRecipeProvider.hasItem(ModItems.TECHNOLOGY), FabricRecipeProvider.conditionsFromItem(ModItems.TECHNOLOGY))
                    .criterion(FabricRecipeProvider.hasItem(ModItems.CARBON_INGOT), FabricRecipeProvider.conditionsFromItem(ModItems.CARBON_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(ModItems.BASIC_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.BASIC_SLOT))
                    .criterion(FabricRecipeProvider.hasItem(ModItems.ASSEMBLY_SLOT), FabricRecipeProvider.conditionsFromItem(ModItems.ASSEMBLY_SLOT))
                    .criterion(FabricRecipeProvider.hasItem(Items.NETHER_STAR), FabricRecipeProvider.conditionsFromItem(Items.NETHER_STAR))
                    .offerTo(exporter);
        });

    }

    record UpgradeCoreRecipeEntry(Item result, Item ring1, Item ring2, Item ring3) {

    }

    public static class ShapedAssemblyRecipeJsonBuilder extends ShapedRecipeJsonBuilder {
        public ShapedAssemblyRecipeJsonBuilder(RecipeCategory category, ItemConvertible output, int count) {
            super(category, output, count);
        }

        public static ShapedAssemblyRecipeJsonBuilder create(RecipeCategory category, ItemConvertible output) {
            return create(category, output, 1);
        }

        public static ShapedAssemblyRecipeJsonBuilder create(RecipeCategory category, ItemConvertible output, int count) {
            return new ShapedAssemblyRecipeJsonBuilder(category, output, count);
        }

        @Override
        public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
            AtomicReference<RecipeJsonProvider> originalProviderReference = new AtomicReference<>();
            Consumer<RecipeJsonProvider> trap = originalProviderReference::set;

            super.offerTo(trap, recipeId);
            var originalProvider = originalProviderReference.get();

            exporter.accept(new RecipeJsonProvider() {
                @Override
                public void serialize(JsonObject json) {
                    originalProvider.serialize(json);
                }

                @Override
                public Identifier getRecipeId() {
                    return originalProvider.getRecipeId();
                }

                @Override
                public RecipeSerializer<?> getSerializer() {
                    return AssemblerRecipe.Serializer.INSTANCE;
                }

                @Nullable
                @Override
                public JsonObject toAdvancementJson() {
                    return originalProvider.toAdvancementJson();
                }

                @Nullable
                @Override
                public Identifier getAdvancementId() {
                    return originalProvider.getAdvancementId();
                }
            });
        }
    }
}