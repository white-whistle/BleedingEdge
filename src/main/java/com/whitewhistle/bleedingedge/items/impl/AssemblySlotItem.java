package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.recipe.AssemblerRecipe;
import com.whitewhistle.bleedingedge.util.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import org.joml.Vector2i;

import java.util.List;
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

    private boolean craftSuccess(PlayerEntity player, CraftingContext ctx) {
        player.playSound(SoundEvents.BLOCK_DECORATED_POT_HIT, r0(), r0());

        if (this != ModItems.CREATIVE_ASSEMBLY_SLOT) {
            decrementIngredients(player, ctx);
        }

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

        if (ctx.output.isEmpty()) {
            return craftFail(player);
        }

        if (!cursorStack.isEmpty()) {
            if (!ItemStack.canCombine(cursorStack, ctx.output)) {
                return craftFail(player);
            }

            ItemStackUtil.joinStacks(cursorStack, ctx.output);

            return craftSuccess(player, ctx);
        }

        cursorStackReference.set(ctx.output);

        return craftSuccess(player, ctx);
    }

    private void decrementIngredients(PlayerEntity player, CraftingContext ctx) {
        IntStream.range(0, ctx.dimensions.size()).forEach(i -> {
            var slotStack = ctx.slotStacks.get(i);
            if (slotStack.isEmpty()) return;

            var processingStack = ctx.processingStacks.get(i);
            var remainder = processingStack.getRecipeRemainder();

            processingStack.decrement(1);

            if (processingStack.isEmpty()) {
                if (remainder.isEmpty()) {
                    ModComponents.PROCESSING_STACK.remove(slotStack);
                } else {
                    ModComponents.PROCESSING_STACK.set(slotStack, remainder);
                }
            } else {
                ModComponents.PROCESSING_STACK.set(slotStack, processingStack);

                if (!remainder.isEmpty()) {
                    player.giveItemStack(remainder);
                }
            }
        });
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

        for (var dim : dims.orientations()) {

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
                    dim.x, dim.y
            );

            IntStream.range(0, dims.size()).forEach(i -> {
                var stack = processingStacks.get(i);
                craftingInv.setStack(i, stack);
            });

            var oAssemblerRecipe = recipeManager.getFirstMatch(AssemblerRecipe.Type.INSTANCE, craftingInv, world);
            if (oAssemblerRecipe.isPresent()) {
                return new CraftingContext(slotStacks, processingStacks, craftingInv, oAssemblerRecipe.get().craft(craftingInv, world.getRegistryManager()), dims);
            }

            var oCraftingRecipe = recipeManager.getFirstMatch(RecipeType.CRAFTING, craftingInv, world);
            if (oCraftingRecipe.isPresent()) {
                return new CraftingContext(slotStacks, processingStacks, craftingInv, oCraftingRecipe.get().craft(craftingInv, world.getRegistryManager()), dims);
            }

        }

        return null;

    }

    record CraftingContext(List<ItemStack> slotStacks, List<ItemStack> processingStacks,
                           CraftingInventory craftingInventory, ItemStack output, CraftingDimensions dimensions) {

    }

    enum CraftingDimensions {
        F1x1(1, 1),
        F2x2(2, 2),
        F2x3(2, 3),
        F3x3(3, 3),
        F3x4(3, 4),
        F4x4(4, 4),
        F4x5(4, 5),
        F5x5(5, 5),
        F9x3(9, 3);

        public final int w;
        public final int h;

        CraftingDimensions(int w, int h) {
            this.w = w;
            this.h = h;
        }

        public int size() {
            return this.w * this.h;
        }

        public List<Vector2i> orientations() {
            var isSquare = this.w == this.h;

            if (isSquare) {
                return List.of(new Vector2i(this.w, this.h));
            }

            return List.of(
                    new Vector2i(this.w, this.h),
                    new Vector2i(this.h, this.w)
            );
        }

        public static CraftingDimensions getDimensions(int size) {
            if (size >= F9x3.size()) return F9x3;
            if (size >= F5x5.size()) return F5x5;
            if (size >= F4x5.size()) return F4x5;
            if (size >= F4x4.size()) return F4x4;
            if (size >= F3x4.size()) return F3x4;
            if (size >= F3x3.size()) return F3x3;
            if (size >= F2x3.size()) return F2x3;
            if (size >= F2x2.size()) return F2x2;

            if (size == 1) return F1x1;
            return null;
        }
    }
}
