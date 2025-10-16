package com.whitewhistle.bleedingedge.items.impl;

import com.google.common.collect.Multimap;
import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.IHasHotkeys;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;

import java.util.Set;
import java.util.UUID;

import static com.whitewhistle.bleedingedge.util.RandomUtil.r0;

public class ObsidianSashItem extends TrinketItem implements IHasHotkeys {

    public ObsidianSashItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var ret = super.getModifiers(stack, slot, entity, uuid);

        ret.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uuid, ModIdentifier.string("obsidian_sash_armor"), 2, EntityAttributeModifier.Operation.ADDITION));

        return ret;
    }

    @Override
    public void handleHotkey(Identifier packetId, PlayerEntity player, ItemStack stack) {
        if (player.hasStatusEffect(ModStatusEffects.EMP)) return;

        if (player.hasStatusEffect(ModStatusEffects.QUANTUM_TUNNELING)) {
            player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.CANCEL_QUANTUM_TUNNELING, 1));
            player.removeStatusEffect(ModStatusEffects.QUANTUM_TUNNELING);
            player.removeStatusEffect(ModStatusEffects.CANCEL_QUANTUM_TUNNELING);
        } else {
            player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.QUANTUM_TUNNELING, 20 * 3));
        }
    }

    private static RegistryKey<World> getNextDimension(World world) {
        var registryKey = world.getRegistryKey();

        if (registryKey == World.NETHER) return World.OVERWORLD;
        if (registryKey == World.OVERWORLD) return World.NETHER;

        return null;
    }

    public static void shiftEntityDimension(LivingEntity livingEntity) {
        var world = livingEntity.getWorld();

        if (!world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) world;

            MinecraftServer minecraftServer = serverWorld.getServer();

            var destinationDimKey = getNextDimension(world);
            var dimensionValid = destinationDimKey != null;

            if (!dimensionValid) {
                livingEntity.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, r0(), r0());
                return;
            }

            ServerWorld destinationWorld = minecraftServer.getWorld(destinationDimKey);

            if (destinationWorld != null && minecraftServer.isNetherAllowed() && !livingEntity.hasVehicle()) {

                if (livingEntity.isRemoved()) {
                    return;
                }

                double d = DimensionType.getCoordinateScaleFactor(livingEntity.getWorld().getDimension(), destinationWorld.getDimension());
                WorldBorder worldBorder = destinationWorld.getWorldBorder();
                BlockPos destinationPos = worldBorder.clamp(livingEntity.getX() * d, livingEntity.getY(), livingEntity.getZ() * d);
                livingEntity.teleport(destinationWorld, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), Set.of(), livingEntity.getYaw(), livingEntity.getPitch());

            }
        }
    }
}
