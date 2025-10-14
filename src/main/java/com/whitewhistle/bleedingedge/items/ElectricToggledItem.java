package com.whitewhistle.bleedingedge.items;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface ElectricToggledItem extends ToggledItem {

    default boolean isEnabled(LivingEntity livingEntity, ItemStack stack) {
        return !livingEntity.hasStatusEffect(ModStatusEffects.EMP) && ModComponents.ENABLED.get(stack);
    }
}
