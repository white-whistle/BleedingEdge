package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

public class SteelKidneyItem extends ModTrinketItem implements ElectricToggledItem.WithAbilities {
    public SteelKidneyItem(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        if (this.isEnabled(entity, stack)) {
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.ANTIDOTE, 20, 0, true, false, false));
        }
    }
}
