package com.whitewhistle.bleedingedge.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;

import java.util.Optional;
import java.util.Random;

public class GlobalItemClick {

    private static final Random r = new Random();

    public static Optional<Boolean> onClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        var item = stack.getItem();
        var cursorStack = cursorStackReference.get();
        var cursorItem = cursorStack.getItem();

        if (clickType == ClickType.RIGHT) {
            if (!stack.isEmpty() && !(item instanceof AppliedItem) && cursorItem instanceof AppliedItem appliedItem) {
                if (appliedItem.apply(cursorStack, stack)) {
                    cursorStack.decrement(1);
                    player.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 0.5f + r.nextFloat(), 0.5f + r.nextFloat());
                } else {
                    player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5f + r.nextFloat(), 0.5f + r.nextFloat());
                }
                return Optional.of(true);
            }
        }

        return Optional.empty();
    }
}
