package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RepairSlotItem extends SlotItem{
    public RepairSlotItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (ModComponents.PROCESSING_STACK.has(stack)) {
            var processingStack = ModComponents.PROCESSING_STACK.get(stack);
            var damage = processingStack.getDamage();
            if (damage > 0) {
                var newDamage = damage - 1;
                processingStack.setDamage(newDamage);
            }
        }
    }

    @Override
    public boolean isItemApplicable(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        return cursorStack.isDamageable();
    }
}
