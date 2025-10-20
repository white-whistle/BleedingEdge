package com.whitewhistle.bleedingedge.items.impl;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class SpidercoreItem extends ModTrinketItem{
    public SpidercoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getThreatLevel() {
        return EXTREME_THREAT;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);

        SlotAttributes.addSlotModifier(modifiers, "head/face", uuid, 2, EntityAttributeModifier.Operation.ADDITION);
        SlotAttributes.addSlotModifier(modifiers, "feet/shoes", uuid, 2, EntityAttributeModifier.Operation.ADDITION);

        modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "tacticore move speed", 0.5, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(uuid, "tacticore damage", 4, EntityAttributeModifier.Operation.ADDITION));
        modifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(uuid, "tacticore damage", 0.25, EntityAttributeModifier.Operation.MULTIPLY_BASE));

        return modifiers;
    }
}
