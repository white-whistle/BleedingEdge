package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class TeslaPackItem extends ModTrinketItem implements ElectricToggledItem.WithAbilities {

    private static final int ENEMY_TESLA_DAMAGE = 6;
    private static final int SELF_TESLA_DAMAGE = 2;

    public TeslaPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getThreatLevel() {
        return ModTrinketItem.MAJOR_THREAT;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        var world = entity.getWorld();

        if (!world.isClient && entity instanceof PlayerEntity player) {
            var enabled = isEnabled(entity, stack);

            if (enabled) {
                var entitiesInRange = world.getOtherEntities(player, player.getBoundingBox().expand(5, 1, 5));
                var damageSource = world.getDamageSources().lightningBolt();

                entitiesInRange.forEach(e -> {
                    if (e instanceof LivingEntity livingEntity) {
                        livingEntity.damage(damageSource, ENEMY_TESLA_DAMAGE);
                        livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, 20 * 3));
                    }
                });

                player.damage(damageSource, SELF_TESLA_DAMAGE);
                player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, 20 * 3));

            }
        }
    }
}
