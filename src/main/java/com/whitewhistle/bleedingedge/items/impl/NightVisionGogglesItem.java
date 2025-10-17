package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;

public class NightVisionGogglesItem extends ModTrinketItem implements ElectricToggledItem.WithAbilities {
    public NightVisionGogglesItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getThreatLevel() {
        return ModTrinketItem.MODERATE_THREAT;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        if (this.isEnabled(entity, stack)) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20, 0, true, false, false));
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.IR_VISION, 20, 0, true, false, false));
        } else {
            entity.removeStatusEffect(ModStatusEffects.IR_VISION);
        }
    }

}
