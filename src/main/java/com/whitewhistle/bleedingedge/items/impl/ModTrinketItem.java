package com.whitewhistle.bleedingedge.items.impl;

import com.google.common.collect.Multimap;
import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ModTrinketItem extends TrinketItem {
    public static final int MINOR_THREAT = 1;
    public static final int MODERATE_THREAT = 3;
    public static final int MAJOR_THREAT = 10;
    public static final int EXTREME_THREAT = 20;

    public ModTrinketItem(Settings settings) {
        super(settings);
    }

    public int getThreatLevel() {
        return MINOR_THREAT;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var ret = super.getModifiers(stack, slot, entity, uuid);

        ret.put(ModEntityAttributes.THREAT, new EntityAttributeModifier(uuid, "bleeding-edge:trinket-item-threat", getThreatLevel(), EntityAttributeModifier.Operation.ADDITION));

        return ret;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return ModComponents.ENABLED.get(stack);
    }
}
