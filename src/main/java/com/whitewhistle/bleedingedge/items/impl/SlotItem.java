package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotItem extends Item {
    public SlotItem(Settings settings) {
        super(settings);
    }

    public boolean isItemApplicable(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        return true;
    }

    public boolean isItemApplicableCatalyst(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        return false;
    }

    public void onItemApplyCatalyst(PlayerEntity player, ItemStack stack, ItemStack cursorStack, StackReference cursorStackReference) {

    }

    public void onItemApplySuccess(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {

    }

    public void onEmptied(PlayerEntity player, ItemStack stack) {

    }

    public void onItemApplyFail(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {

    }

    /**
     * Handles a right click of an item into this slot item, using #isItemApplicable to determine if item can enter the slot
     * @param player the player
     * @param stack the SlotItem stack
     * @param cursorStackReference stack reference to the cursor stack
     * @return true if interaction was handled
     */
    public boolean applyItemToSlot(PlayerEntity player, ItemStack stack, StackReference cursorStackReference) {
        var processingStack = ModComponents.PROCESSING_STACK.get(stack);
        var cursorStack = cursorStackReference.get();

        if (cursorStack.isEmpty()) {
            if (processingStack.isEmpty()) {
                return false;
            }

            ModComponents.PROCESSING_STACK.remove(stack);
            cursorStackReference.set(processingStack);

            this.onEmptied(player, stack);

        } else {
            if (this.isItemApplicableCatalyst(player, stack, cursorStack)) {
                this.onItemApplyCatalyst(player, stack, cursorStack, cursorStackReference);

                return true;
            }
            if (!this.isItemApplicable(player, stack, cursorStack)) {
                this.onItemApplyFail(player, stack, cursorStack);

                return true;
            }

            // swap processing stacks
            if (!processingStack.isEmpty()) {
                ModComponents.PROCESSING_STACK.set(stack, cursorStack);
                cursorStackReference.set(processingStack);

                this.onItemApplySuccess(player, stack, cursorStack);

                return true;
            }

            ModComponents.PROCESSING_STACK.set(stack, cursorStack);
            cursorStackReference.set(ItemStack.EMPTY);

            this.onItemApplySuccess(player, stack, cursorStack);
        }

        return true;
    }
}
