package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.ModDamageTypes;
import com.whitewhistle.bleedingedge.items.ModItems;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrainDrillItem extends ModTrinketItem{
    public BrainDrillItem(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        var headStack = entity.getEquippedStack(EquipmentSlot.HEAD);

        if (!headStack.isEmpty()) {
            var eyePos = entity.getEyePos();
            var world = entity.getWorld();
            if (!world.isClient) {
                ((ServerWorld)world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, headStack), eyePos.x, eyePos.y, eyePos.z, 1, 0, -1, 0,0.2);
            }

            headStack.damage(1, entity, e -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
            });

            return;
        }

        var eyePos = entity.getEyePos();
        var world = entity.getWorld();
        if (!world.isClient) {
            ((ServerWorld)world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, getEntityBrain(entity).getDefaultStack()), eyePos.x, eyePos.y, eyePos.z, 1, 0, -1, 0,0.2);
        }

        entity.damage(ModDamageTypes.of(entity.getWorld(), ModDamageTypes.BRAIN_DAMAGE), 1);

    }

    @Override
    public int getThreatLevel() {
        return 0;
    }

    public static Item getEntityBrain(LivingEntity entity) {
        var o = TrinketsApi.getTrinketComponent(entity);
        if (o.isPresent()) {
            var trinkets = o.get();

            var coreModifiers = trinkets.getModifiers().get("head/core");
            if (coreModifiers.stream().anyMatch(m -> m.getId().equals(BrainItem.STEEL_BRAIN_CORE_SLOT_UUID))) {
                return ModItems.STEEL_BRAIN;
            }
        }

        if (entity instanceof VillagerEntity) {
            return ModItems.BRAIN;
        }

        if (entity instanceof MobEntity) {
            return ModItems.ROTTEN_BRAIN;
        }

        return ModItems.SMALL_BRAIN;
    }

    public static void registerDeathListeners() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (damageSource.isOf(ModDamageTypes.BRAIN_DAMAGE)) {
                entity.dropItem(getEntityBrain(entity));

                var o = TrinketsApi.getTrinketComponent(entity);
                if (o.isPresent()) {
                    var trinkets = o.get();

                    var coreModifiers = trinkets.getModifiers().get("head/core");
                    if (coreModifiers.stream().anyMatch(m -> m.getId().equals(BrainItem.STEEL_BRAIN_CORE_SLOT_UUID))) {
                        trinkets.removeModifiers(BrainItem.STEEL_BRAIN_MODIFIERS);
                    }
                }
            }
        });
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        System.out.println("Used on villager?");
        if (equipItem(entity, stack)) {
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.FAIL;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable("tooltip.bleeding-edge.brain_drill.description1").styled(s -> s.withColor(Formatting.GRAY)));
        tooltip.add(Text.translatable("tooltip.bleeding-edge.brain_drill.description2").styled(s -> s.withColor(Formatting.GRAY)));
        tooltip.add(Text.translatable("tooltip.bleeding-edge.brain_drill.description3").styled(s -> s.withColor(Formatting.GRAY)));
    }

    @Override
    public boolean canSpawnWithMob(MobEntity mob) {
        return false;
    }
}
