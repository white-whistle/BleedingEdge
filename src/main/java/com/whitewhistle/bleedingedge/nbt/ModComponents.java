package com.whitewhistle.bleedingedge.nbt;

import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.item.ItemStack;

public class ModComponents {

    public static final ItemComponent<Boolean> ENABLED = new ItemComponent<>(ModIdentifier.of("enabled"), ModCodecs.BOOLEAN);
    public static final ItemComponent<Boolean> CHARGED = new ItemComponent<>(ModIdentifier.of("charged"), ModCodecs.BOOLEAN);
    public static final ItemComponent<ItemStack> PROCESSING_STACK = new ItemComponent<>(ModIdentifier.of("charged"), ModCodecs.ITEMSTACK);
    public static final ItemComponent<Integer> CURRENT_BURN_TICKS = new ItemComponent<>(ModIdentifier.of("current_burn_ticks"), ModCodecs.INT);
    public static final ItemComponent<Integer> TOTAL_BURN_TICKS = new ItemComponent<>(ModIdentifier.of("total_burn_ticks"), ModCodecs.INT);

    public static void register() {

    }
}
