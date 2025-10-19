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

public class HeavycoreItem extends ModTrinketItem{
    public HeavycoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getThreatLevel() {
        return MODERATE_THREAT;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);

        SlotAttributes.addSlotModifier(modifiers, "chest/necklace", uuid, -1, EntityAttributeModifier.Operation.ADDITION);
        SlotAttributes.addSlotModifier(modifiers, "legs/belt", uuid, -1, EntityAttributeModifier.Operation.ADDITION);
        SlotAttributes.addSlotModifier(modifiers, "chest/back", uuid, 1, EntityAttributeModifier.Operation.ADDITION);

        modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "tacticore move speed", -0.2, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        modifiers.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(uuid, "tacticore health", 10, EntityAttributeModifier.Operation.ADDITION));
        modifiers.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uuid, "tacticore armor", 8, EntityAttributeModifier.Operation.ADDITION));
        modifiers.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uuid, "tacticore armor", 4, EntityAttributeModifier.Operation.ADDITION));

        return modifiers;
    }
}
