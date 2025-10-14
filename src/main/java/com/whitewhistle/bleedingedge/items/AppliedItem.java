package com.whitewhistle.bleedingedge.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface AppliedItem {
    /**
     * called when the user tries to apply the item to another item
     * @param stack the item to be applied onto a different item
     * @param target item stack to apply the item to
     * @return true if the item was applied successfully
     */
    boolean apply(PlayerEntity player, ItemStack stack, ItemStack target);

    /**
     * called to determine if to decrement the AppliedItem stack after the item was applied
     * @param stack the item to be applied onto a different item
     * @param target item stack to apply the item to
     * @return if the applied item stack should decrement
     */
    default boolean shouldConsume(ItemStack stack, ItemStack target) {
        return true;
    }

    default Text getTooltip(ItemStack stack) {
        return Text.translatable("tooltip.bleeding-edge.applied-item.description");
    }
}
