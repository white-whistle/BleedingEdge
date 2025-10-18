package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import com.whitewhistle.bleedingedge.particles.ModParticleTypes;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.whitewhistle.bleedingedge.util.RandomUtil.r0;

public class TeslaPackItem extends ModTrinketItem implements ElectricToggledItem.WithAbilities {

    private static final int ENEMY_TESLA_DAMAGE = 10;
    private static final int SELF_TESLA_DAMAGE = 5;

    public TeslaPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable("tooltip.bleeding-edge.tesla_pack.description1").styled(s -> s.withColor(Formatting.GRAY)));
        tooltip.add(Text.translatable("tooltip.bleeding-edge.tesla_pack.description2").styled(s -> s.withColor(Formatting.GRAY)));
    }

    @Override
    public int getThreatLevel() {
        return ModTrinketItem.MAJOR_THREAT;
    }

    public static void spawnTeslaBlast(World world, Entity except, double x, double y, double z, float width, float height, int damage, int duration, int particleCount) {
        var entitiesInRange = world.getOtherEntities(except, new Box(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5).expand(width, height, width));
        var damageSource = world.getDamageSources().lightningBolt();

        entitiesInRange.forEach(e -> {
            if (e instanceof LivingEntity livingEntity) {
                livingEntity.damage(damageSource, damage);
                livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, duration));
            }
        });

        world.playSound(null, x,y,z, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 1f, r0() * 3.2f);

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ModParticleTypes.ELECTRICITY, x,y,z, particleCount, 0, 0, 0, 0);
        }

    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        var world = entity.getWorld();

        if (!world.isClient) {
            var enabled = isEnabled(entity, stack);

            if (enabled) {
                spawnTeslaBlast(world, entity, entity.getX(), entity.getY(), entity.getZ(), 5, 1, ENEMY_TESLA_DAMAGE, 20 * 3, 64);

                var damageSource = world.getDamageSources().lightningBolt();

                entity.damage(damageSource, SELF_TESLA_DAMAGE);
                entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, 20 * 3));
            }
        }
    }
}
