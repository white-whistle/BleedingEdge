package com.whitewhistle.bleedingedge.items.impl;

import com.google.common.collect.Multimap;
import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ShieldGeneratorItem extends ModTrinketItem implements ElectricToggledItem.WithAbilities {

    public ShieldGeneratorItem(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        if (entity.hasStatusEffect(ModStatusEffects.SHIELD_COOLDOWN)) return;
        if (!this.isEnabled(entity, stack)) return;

        var currentShield = entity.getAbsorptionAmount();
        var maxShield = entity.getAttributeValue(ModEntityAttributes.SHIELD);

        if (currentShield < maxShield) {
            entity.setAbsorptionAmount(currentShield + 1);
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onUnequip(stack, slot, entity);

        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - 20);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var ret= super.getModifiers(stack, slot, entity, uuid);

        ret.put(ModEntityAttributes.SHIELD, new EntityAttributeModifier(uuid, "bleeding-edge:personal-shield-max-shield",20, EntityAttributeModifier.Operation.ADDITION));

        return ret;
    }
}
