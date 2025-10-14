package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HeatingSlotItem extends SlotItem {
    private static final Random r = new Random();
    private static float r0() {
        return 0.5f + (r.nextFloat() * 0.5f);
    }

    public HeatingSlotItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!(entity instanceof PlayerEntity player)) return;
        if (!ModComponents.PROCESSING_STACK.has(stack)) return;

        var totalBurnTicks = ModComponents.TOTAL_BURN_TICKS.get(stack);
        if (totalBurnTicks <= 0) return;

        var newValue = ModComponents.CURRENT_BURN_TICKS.get(stack) + 1;

        SmeltingRecipe smeltingRecipe = null;
        ItemStack processingStack = null;

        while (newValue >= totalBurnTicks) {
            if (smeltingRecipe == null) {
                processingStack = ModComponents.PROCESSING_STACK.get(stack);
                smeltingRecipe = getSmeltingRecipe(player, processingStack).orElseThrow();
            }


            newValue -= totalBurnTicks;

            player.giveItemStack(smeltingRecipe.getOutput(world.getRegistryManager()).copy());
            player.playSound(SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.AMBIENT, r0() , r0());

            processingStack.decrement(1);
            if (processingStack.isEmpty()) {
                break;
            }

        }

        if (processingStack != null) {
            if (processingStack.isEmpty()) {
                ModComponents.PROCESSING_STACK.remove(stack);
            } else {
                ModComponents.PROCESSING_STACK.set(stack,processingStack);
            }
        }

        ModComponents.CURRENT_BURN_TICKS.set(stack, newValue);

    }

    @Override
    public boolean isItemApplicable(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        var recipe = getSmeltingRecipe(player, cursorStack);

        return recipe.isPresent();
    }

    private Optional<SmeltingRecipe> getSmeltingRecipe(PlayerEntity player, ItemStack stack) {
        var world = player.getWorld();
        var recipeManager = world.getRecipeManager();

        return recipeManager.getFirstMatch(RecipeType.SMELTING, new SimpleInventory(stack), world);
    }

    @Override
    public void onItemApplySuccess(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        super.onItemApplySuccess(player, stack, cursorStack);

        player.playSound(SoundEvents.BLOCK_DECORATED_POT_HIT, SoundCategory.PLAYERS, r0() , r0());

        getSmeltingRecipe(player, cursorStack).ifPresent(recipe -> {
            ModComponents.CURRENT_BURN_TICKS.set(stack, 0);
            ModComponents.TOTAL_BURN_TICKS.set(stack, recipe.getCookTime());
        });

    }

    @Override
    public void onItemApplyFail(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        super.onItemApplyFail(player, stack, cursorStack);
    }

    @Override
    public void onEmptied(PlayerEntity player, ItemStack stack) {
        super.onEmptied(player, stack);

        player.playSound(SoundEvents.BLOCK_DECORATED_POT_HIT, SoundCategory.PLAYERS, r0() , r0());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable("tooltip.bleeding-edge.heating_slot.description1").styled(s -> s.withColor(Formatting.GRAY)));
        tooltip.add(Text.translatable("tooltip.bleeding-edge.heating_slot.description2").styled(s -> s.withColor(Formatting.GRAY)));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return ModComponents.PROCESSING_STACK.has(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xe59a3e;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        var currentBurnTicks = ModComponents.CURRENT_BURN_TICKS.get(stack);
        var totalBurnTicks = ModComponents.TOTAL_BURN_TICKS.get(stack);

        return Math.round(13f * currentBurnTicks / totalBurnTicks);
    }

    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return new ItemStack(this);
    }
}
