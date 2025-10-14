package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

public class CloakingDeviceItem extends TrinketItem implements ElectricToggledItem {
    public CloakingDeviceItem(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        if (!entity.hasStatusEffect(ModStatusEffects.EMP) && ModComponents.ENABLED.get(stack)) {
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.CLOAKED, 20, 0, true, false, false));
        } else {
            entity.removeStatusEffect(ModStatusEffects.CLOAKED);
        }
    }

    // @Override
    // public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
    //     super.inventoryTick(stack, world, entity, slot, selected);
    //
    //     if (entity instanceof LivingEntity livingEntity) {
    //         if (ModComponents.ENABLED.get(stack)) {
    //             livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.CLOAKED, 20, 0, true, false, false));
    //         } else {
    //             livingEntity.removeStatusEffect(ModStatusEffects.CLOAKED);
    //         }
    //     }
    // }
}
