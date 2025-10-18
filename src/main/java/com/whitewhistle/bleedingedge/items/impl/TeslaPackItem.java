package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import com.whitewhistle.bleedingedge.particles.ModParticles;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import static com.whitewhistle.bleedingedge.util.RandomUtil.r0;

public class TeslaPackItem extends ModTrinketItem implements ElectricToggledItem.WithAbilities {

    private static final int ENEMY_TESLA_DAMAGE = 10;
    private static final int SELF_TESLA_DAMAGE = 5;

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

        if (!world.isClient) {
            var enabled = isEnabled(entity, stack);

            if (enabled) {
                var entitiesInRange = world.getOtherEntities(entity, entity.getBoundingBox().expand(5, 1, 5));
                var damageSource = world.getDamageSources().lightningBolt();

                entitiesInRange.forEach(e -> {
                    if (e instanceof LivingEntity livingEntity) {
                        livingEntity.damage(damageSource, ENEMY_TESLA_DAMAGE);
                        livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, 20 * 3));
                    }
                });

                entity.damage(damageSource, SELF_TESLA_DAMAGE);
                entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, 20 * 3));

                world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 1f, r0() * 3.2f);


                ((ServerWorld)world).spawnParticles(ModParticles.ELECTRICITY, entity.getX(), entity.getY(), entity.getZ(), 64, 0, 0, 0, 0);
            }
        }
    }
}
