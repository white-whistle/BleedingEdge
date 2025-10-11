package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.AppliedItem;
import com.whitewhistle.bleedingedge.items.ToggledItem;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NightVisionGogglesItem extends ArmorItem implements AppliedItem, ToggledItem {
    public NightVisionGogglesItem(Settings settings) {
        super(ArmorMaterials.IRON, Type.HELMET, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getEquippedStack(this.getSlotType()).isOf(this)) {
                if (ModComponents.ENABLED.get(stack)) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20, 0, true, false, false));
                    livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.IR_VISION, 20, 0, true, false, false));
                } else {
                    livingEntity.removeStatusEffect(ModStatusEffects.IR_VISION);
                }
            }
        }
    }

    @Override
    public boolean apply(ItemStack stack, ItemStack target) {
        return false;
    }
}
