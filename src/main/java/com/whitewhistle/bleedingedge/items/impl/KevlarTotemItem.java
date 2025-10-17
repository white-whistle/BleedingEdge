package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.network.S2CSender;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;

public class KevlarTotemItem extends ModTrinketItem{
    public KevlarTotemItem(Settings settings) {
        super(settings);
    }

    public static boolean tryUseKevlarTotem(LivingEntity entity, DamageSource source) {
            if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;

            if (entity.hasStatusEffect(ModStatusEffects.EMP)) return false;

            var optionalComponent = TrinketsApi.getTrinketComponent(entity);
            if (optionalComponent.isEmpty()) return false;

            var trinketComponent = optionalComponent.get();
            if (!trinketComponent.isEquipped(ModItems.KEVLAR_TOTEM)) return false;

            var equippedTotems = trinketComponent.getEquipped(ModItems.KEVLAR_TOTEM);
            if (equippedTotems.size() == 0) return false;

            var totemRef = equippedTotems.get(0);
            var slot = totemRef.getLeft();

            entity.setHealth(1.0F);
            entity.clearStatusEffects();
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1, false, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1, false, false, true));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0, false, false, true));
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.CLOAKED, 20 * 20, 0, false, false, true));
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, 20 * 10, 0, false, false, true));

            totemRef.getRight().decrement(1);

            if (entity instanceof ServerPlayerEntity serverPlayer) {
                S2CSender.sendKevlarTotemTrigger(serverPlayer);
            }

            return true;
    }


}
