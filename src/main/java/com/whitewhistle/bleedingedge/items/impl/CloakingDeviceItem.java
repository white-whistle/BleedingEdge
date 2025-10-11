package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ToggledItem;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CloakingDeviceItem extends Item implements ToggledItem {
    public CloakingDeviceItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof LivingEntity livingEntity) {
            if (ModComponents.ENABLED.get(stack)) {
                livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.CLOAKED, 20, 0, true, false, false));
            } else {
                livingEntity.removeStatusEffect(ModStatusEffects.CLOAKED);
            }
        }
    }
}
