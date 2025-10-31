package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.util.ItemStackUtil;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.Optional;

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
     *
     * @param player               the player
     * @param stack                the SlotItem stack
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

            var splitStack = processingStack.split(1);

            if (processingStack.isEmpty()) {
                ModComponents.PROCESSING_STACK.remove(stack);
                this.onEmptied(player, stack);
            } else {
                ModComponents.PROCESSING_STACK.set(stack, processingStack);
            }

            cursorStackReference.set(splitStack);

        } else {
            if (this.isItemApplicableCatalyst(player, stack, cursorStack)) {
                this.onItemApplyCatalyst(player, stack, cursorStack, cursorStackReference);

                return true;
            }
            if (!this.isItemApplicable(player, stack, cursorStack)) {
                this.onItemApplyFail(player, stack, cursorStack);

                return true;
            }

            if (processingStack.isEmpty()) {
                var splitStack = cursorStack.split(1);
                ModComponents.PROCESSING_STACK.set(stack, splitStack);

                this.onItemApplySuccess(player, stack, splitStack);

                return true;
            }

            if (ItemStack.canCombine(processingStack, cursorStack)) {
                cursorStack.decrement(1);
                processingStack.increment(1);

                ModComponents.PROCESSING_STACK.set(stack, processingStack);

                this.onItemApplySuccess(player, stack, cursorStack);

                return true;
            }

            // swap processing stacks
            if (processingStack.getCount() == 1 && cursorStack.getCount() == 1) {
                // if both are single items, swap
                ModComponents.PROCESSING_STACK.set(stack, cursorStack);
                cursorStackReference.set(processingStack);

                this.onItemApplySuccess(player, stack, cursorStack);

                return true;
            }

            this.onItemApplyFail(player, stack, cursorStack);
            return false;
            // ModComponents.PROCESSING_STACK.set(stack, cursorStack);
            // cursorStackReference.set(ItemStack.EMPTY);

        }

        return true;
    }

    public boolean swapContentsWithSlot(PlayerEntity player, Slot slot, ItemStack stack, StackReference cursorStackReference) {
        var slotStack = cursorStackReference.get();
        var processingStack = ModComponents.PROCESSING_STACK.get(slotStack);

        if (processingStack.isEmpty() && stack.isEmpty()) return false;

        if (stack.isEmpty()) {
            ModComponents.PROCESSING_STACK.remove(slotStack);
            slot.setStack(processingStack);
            this.onEmptied(player, slotStack);

            return true;
        }

        if (!this.isItemApplicable(player, slotStack, stack)) {
            return false;
        }

        // consume stack
        if (processingStack.isEmpty()) {
            ModComponents.PROCESSING_STACK.set(slotStack, stack);
            slot.setStack(ItemStack.EMPTY);

            this.onItemApplySuccess(player, slotStack, stack);

            return true;
        }

        // join stacks
        if (ItemStack.canCombine(processingStack, stack)) {
            ItemStackUtil.joinStacks(processingStack, stack);
            ModComponents.PROCESSING_STACK.set(slotStack, processingStack);

            this.onItemApplySuccess(player, slotStack, stack);

            return true;
        }

        // swap stacks
        ModComponents.PROCESSING_STACK.set(slotStack, stack);
        slot.setStack(processingStack);

        this.onItemApplySuccess(player, slotStack, stack);

        return true;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        if (ModComponents.PROCESSING_STACK.has(stack)) {
            return Optional.of(new SlotItemTooltipData(ModComponents.PROCESSING_STACK.get(stack)));
        }

        return Optional.empty();

    }

    public boolean isLargeSlot() {
        return true;
    }

    public record SlotItemTooltipData(ItemStack stack) implements TooltipData {
    }

}
