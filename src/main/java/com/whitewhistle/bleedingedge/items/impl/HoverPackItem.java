package com.whitewhistle.bleedingedge.items.impl;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class HoverPackItem extends TrinketItem {

    public static final AbilitySource HOVER_PACK_FLIGHT = Pal.getAbilitySource(MOD_ID, "ends_bounty_flight");

    public HoverPackItem(Settings settings) {
        super(settings);
    }

    // @Override
    // public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
    //     super.tick(stack, slot, entity);
    //
    //     if (entity instanceof PlayerEntity player) {
    //
    //     }
    // }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);

        if (!entity.getWorld().isClient && entity instanceof PlayerEntity player) {
            HOVER_PACK_FLIGHT.grantTo(player, VanillaAbilities.ALLOW_FLYING);
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onUnequip(stack, slot, entity);

        if (!entity.getWorld().isClient && entity instanceof PlayerEntity player) {
            HOVER_PACK_FLIGHT.revokeFrom(player, VanillaAbilities.ALLOW_FLYING);
        }
    }
}
