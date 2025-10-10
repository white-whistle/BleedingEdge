package com.whitewhistle.bleedingedge.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class ModComponents {

    public static final ItemComponent<ItemStack> ENABLED = new ItemComponent<>(new Identifier(MOD_ID, "enabled"), ModCodecs.ITEMSTACK);

    public static void register() {

    }
}
