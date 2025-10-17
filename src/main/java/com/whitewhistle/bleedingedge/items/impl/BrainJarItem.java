package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.common.CommonBridge;
import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.util.TweenUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import static com.whitewhistle.bleedingedge.util.RandomUtil.r0;

public class BrainJarItem extends SlotItem{

    public static final int EUREKA_CALORIE_COST = 20_000;
    public static final int HUNGER_CALORIE_MULTIPLIER = 1000;

    public BrainJarItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return ModComponents.PROCESSING_STACK.has(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xfffdc9;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return (int) TweenUtil.lerp(0, 13, ModComponents.EUREKA_POINTS.get(stack) / (float) EUREKA_CALORIE_COST);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!(entity instanceof PlayerEntity player)) return;

        var calories = ModComponents.CALORIES.get(stack);
        if (calories <= 0) return;

        var brainPower = ModComponents.BRAIN_POWER.get(stack);
        if (brainPower <= 0) return;

        var caloriesTaken = Math.min(calories, brainPower);
        calories -= caloriesTaken;

        var ePoints = ModComponents.EUREKA_POINTS.get(stack);
        ePoints+= caloriesTaken;

        while (ePoints >= EUREKA_CALORIE_COST) {
            ePoints -=EUREKA_CALORIE_COST;

            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, r0(), r0());

            player.giveItemStack(ModItems.EUREKA.getDefaultStack());
        }

        ModComponents.EUREKA_POINTS.set(stack, ePoints);
        ModComponents.CALORIES.set(stack, calories);

        if (player.getWorld().isClient) {
            CommonBridge.INSTANCE.showBrainThinkParticles(stack, 1);
        }

    }

    @Override
    public boolean isItemApplicable(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        if (cursorStack.getCount() > 1) return false;

        var cursorItem = cursorStack.getItem();

        return cursorItem instanceof BrainItem;
    }

    @Override
    public void onEmptied(PlayerEntity player, ItemStack stack) {
        super.onEmptied(player, stack);

        player.playSound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK, r0(), r0());
        ModComponents.BRAIN_POWER.remove(stack);
    }

    @Override
    public void onItemApplySuccess(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        super.onItemApplySuccess(player, stack, cursorStack);

        player.playSound(SoundEvents.BLOCK_HONEY_BLOCK_PLACE, r0(), r0());

        var cursorItem = cursorStack.getItem();

        if (cursorItem instanceof BrainItem brainItem) {
            ModComponents.BRAIN_POWER.set(stack, brainItem.brainPower);
        }
    }

    @Override
    public boolean isItemApplicableCatalyst(PlayerEntity player, ItemStack stack, ItemStack cursorStack) {
        if (ModComponents.CALORIES.get(stack) > 0) return false;

        return cursorStack.isFood();
    }

    @Override
    public void onItemApplyCatalyst(PlayerEntity player, ItemStack stack, ItemStack cursorStack, StackReference cursorStackReference) {
        var food = cursorStack.getItem().getFoodComponent();
        if (food == null) return;

        var gainedCalories = Math.round(food.getHunger() * (1 + food.getSaturationModifier()) * HUNGER_CALORIE_MULTIPLIER);

        ModComponents.CALORIES.set(stack, gainedCalories);


        if (cursorStack.isOf(Items.GOLDEN_APPLE)) {
            player.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, r0(), r0());

            ModComponents.PROCESSING_STACK.set(stack, ModItems.BRAIN.getDefaultStack());
            ModComponents.BRAIN_POWER.set(stack, ModItems.BRAIN.brainPower);
        } else {
            player.playSound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK, SoundCategory.PLAYERS, r0(), r0());
        }

        cursorStack.decrement(1);


    }
}
