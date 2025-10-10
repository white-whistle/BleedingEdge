package com.whitewhistle.bleedingedge.datagen;

import com.whitewhistle.bleedingedge.items.IHasModel;
import com.whitewhistle.bleedingedge.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CART_STATION_BLOCK);
        // blockStateModelGenerator.registerFlowerPotPlant(ModBlocks.ELDER_LILY_FLOWER, ModBlocks.POTTED_ELDER_LILY_FLOWER, BlockStateModelGenerator.TintType.NOT_TINTED);
        // blockStateModelGenerator.registerFlowerbed(ModBlocks.SPIRIT_PETALS);
        // blockStateModelGenerator.registerPlantPart();
        // blockStateModelGenerator.registerSimpleState(ModBlocks.BEAR_TRAP_BLOCK);
        // blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SUN_RUNE_BLOCK);
        // blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CHISELED_MOSSY_STONE);
        // Wood
        // blockStateModelGenerator.registerTintableCross(ModBlocks.ANCIENT_TREE_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);
        // blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANCIENT_TREE_PLANKS);
        // blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ANCIENT_TREE_LEAVES);
        // blockStateModelGenerator.registerLog(ModBlocks.ANCIENT_TREE_LOG).log(ModBlocks.ANCIENT_TREE_LOG).wood(ModBlocks.ANCIENT_TREE_WOOD);
        // blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_ANCIENT_TREE_LOG).log(ModBlocks.STRIPPED_ANCIENT_TREE_LOG).wood(ModBlocks.STRIPPED_ANCIENT_TREE_WOOD);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (Item item : ModItems.modItems) {
            if (item instanceof IHasModel iHasModel) {
                var model = iHasModel.getBaseModel();
                if (model == null) continue;

                itemModelGenerator.register(item, model);
            } else {
                itemModelGenerator.register(item, Models.GENERATED);
            }
        }
    }
}