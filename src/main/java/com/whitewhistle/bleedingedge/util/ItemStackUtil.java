package com.whitewhistle.bleedingedge.util;

import net.minecraft.item.ItemStack;

public class ItemStackUtil {
    public static void joinStacks(ItemStack target, ItemStack source) {
        // if either is empty, nothing to do
        if (target.isEmpty() || source.isEmpty()) return;

        // check if the two stacks can combine
        if (!ItemStack.canCombine(target, source)) return;

        int max = target.getMaxCount();
        int combined = target.getCount() + source.getCount();

        // fill the target up to max
        target.setCount(Math.min(max, combined));

        // adjust source to reflect what’s left
        int remainder = combined - max;
        source.setCount(Math.max(remainder, 0));
    }

}
