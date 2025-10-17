package com.whitewhistle.bleedingedge.items;

import com.whitewhistle.bleedingedge.ability.Ability;
import com.whitewhistle.bleedingedge.ability.IHasAbilities;
import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface ElectricToggledItem extends ToggledItem {

    default boolean isEnabled(LivingEntity livingEntity, ItemStack stack) {
        return !livingEntity.hasStatusEffect(ModStatusEffects.EMP) && ModComponents.ENABLED.get(stack);
    }

    interface WithAbilities extends ElectricToggledItem, IHasAbilities {
        @Override
        default List<Ability> getAbilities() {
            return List.of(TOGGLE_ABILITY);
        }
    }
}
