package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ToggledItem;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;

public class NightVisionGogglesItem extends TrinketItem implements ToggledItem {
    public NightVisionGogglesItem(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        if (entity.hasStatusEffect(ModStatusEffects.EMP)) return;

        if (ModComponents.ENABLED.get(stack)) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20, 0, true, false, false));
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.IR_VISION, 20, 0, true, false, false));
        } else {
            entity.removeStatusEffect(ModStatusEffects.IR_VISION);
        }
    }

}
