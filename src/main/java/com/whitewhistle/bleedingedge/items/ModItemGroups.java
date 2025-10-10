package com.whitewhistle.bleedingedge.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class ModItemGroups {
    public static final ItemGroup MOD_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "mod_item_group"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.bleeding-edge")).icon(() -> new ItemStack(ModItems.TACTICORE)).entries(((displayContext, entries) -> {
                for (Item item : ModItems.modItems){
                    entries.add(item);
                }
            })).build());

    public static void registerGroups() {

    }

}