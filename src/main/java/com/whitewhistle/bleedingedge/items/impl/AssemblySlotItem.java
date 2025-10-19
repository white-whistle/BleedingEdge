package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.util.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.whitewhistle.bleedingedge.util.RandomUtil.r0;

public class AssemblySlotItem extends Item {

    public AssemblySlotItem(Settings settings) {
        super(settings);
    }

    private boolean craftFail(PlayerEntity player) {
        player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, r0(), r0());
        return true;
    }

    private boolean craftSuccess(PlayerEntity player) {
        player.playSound(SoundEvents.BLOCK_DECORATED_POT_HIT, r0(), r0());
        return true;
    }

    public boolean handleCraft(PlayerEntity player, ItemStack stack, Slot slot, ItemStack cursorStack, StackReference cursorStackReference) {
        if (ItemStackUtil.isStackFull(cursorStack)) {
            return craftFail(player);
        }

        var ctx = getRecipeInInventory(player, slot.inventory);
        if (ctx == null) {
            return craftFail(player);
        }

        if (ctx.oRecipe.isEmpty()) {
            return craftFail(player);
        }

        var recipe = ctx.oRecipe.get();

        var output = recipe.craft(ctx.craftingInventory, player.getWorld().getRegistryManager());

        if (!cursorStack.isEmpty()) {
            if (!ItemStack.canCombine(cursorStack, output)) {
                return craftFail(player);
            }

            ItemStackUtil.joinStacks(cursorStack, output);

            return craftSuccess(player);
        }

        cursorStackReference.set(output);

        return craftSuccess(player);
    }

    private static CraftingContext getRecipeInInventory(PlayerEntity player, Inventory inventory) {
        var world = player.getWorld();
        var recipeManager = world.getRecipeManager();

        var slotStacks = IntStream.range(0, inventory.size())
                .mapToObj(inventory::getStack)
                .filter(stack -> stack.isOf(ModItems.BASIC_SLOT)).toList();

        var processingStacks = slotStacks.stream()
                .map(ModComponents.PROCESSING_STACK::get).toList();

        if (processingStacks.size() == 0) return null;

        var dims = CraftingDimensions.getDimensions(processingStacks.size());
        if (dims == null) return null;

        CraftingInventory craftingInv = new CraftingInventory(
                new ScreenHandler(null, -1) { // dummy handler
                    @Override
                    public ItemStack quickMove(PlayerEntity player, int slot) {
                        return null;
                    }

                    @Override
                    public boolean canUse(PlayerEntity player) {
                        return false;
                    }
                },
                dims.w, dims.h
        );

        IntStream.range(0, processingStacks.size()).forEach(i -> {
            var stack = processingStacks.get(i);
            craftingInv.setStack(i, stack);
        });

        var oRecipe = recipeManager.getFirstMatch(RecipeType.CRAFTING, craftingInv, world);

        return new CraftingContext(slotStacks, processingStacks, craftingInv, oRecipe);
    }

    record CraftingContext(List<ItemStack> slotStacks, List<ItemStack> processingStacks, CraftingInventory craftingInventory, Optional<CraftingRecipe> oRecipe) {

    }

    enum CraftingDimensions {
        F1x1(1, 1),
        F2x2(2, 2),
        F3x3(3, 3),
        F9x3(9, 3);

        public final int w;
        public final int h;

        CraftingDimensions(int w, int h) {
            this.w = w;
            this.h = h;
        }

        public static CraftingDimensions getDimensions(int size) {
            if (size >= 9 * 3) return F9x3;
            if (size >= 3 * 3) return F3x3;
            if (size >= 2 * 2) return F2x2;
            if (size == 1) return F1x1;
            return null;
        }
    }
}
