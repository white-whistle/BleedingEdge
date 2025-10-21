package com.whitewhistle.bleedingedge.entity;

import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.items.impl.ModTrinketItem;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.util.RandomUtil;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MobThreatSystem {

    private static final List<ModTrinketItem> MOD_TRINKET_ITEMS = ModItems.modItems.stream().filter(i -> i instanceof ModTrinketItem).map(i -> (ModTrinketItem) i).toList();

    public static double getScaledThreat(PlayerEntity player) {
        var world = player.getWorld();
        var threat = player.getAttributeValue(ModEntityAttributes.THREAT);;
        var diffScaling = world.getLocalDifficulty(player.getBlockPos());

        return threat * diffScaling.getLocalDifficulty() * 0.45;
    }

    public static void tryEquipEntity(MobEntity mob) {
        TrinketsApi.getTrinketComponent(mob).ifPresent(comp -> {
            var world = mob.getWorld();

            var player = world.getClosestPlayer(mob, 256);
            if (player == null) return;

            var targetThreat = getScaledThreat(player);
            if (targetThreat <= 0) return;

            // Collect available slots (empty ones only)
            List<SlotReference> availableSlots = new ArrayList<>();
            comp.forEach((ref, stack) -> {
                if (stack.isEmpty()) availableSlots.add(ref);
            });

            if (availableSlots.isEmpty() || MOD_TRINKET_ITEMS.isEmpty()) return;

            // Shuffle items for randomness
            List<ModTrinketItem> shuffled = new ArrayList<>(MOD_TRINKET_ITEMS);
            Collections.shuffle(shuffled, RandomUtil.r);

            int totalThreat = 0;

            // Try to fit items greedily while under threat limit
            for (var item : shuffled) {
                if (!item.canSpawnWithMob(mob)) continue;

                int threat = item.getThreatLevel();
                if (totalThreat + threat > targetThreat) continue;

                var gadgetStack = item.getDefaultStack();
                ModComponents.ENABLED.set(gadgetStack, true);
                gadgetStack.addEnchantment(Enchantments.VANISHING_CURSE, 1);

                // Equip item
                if (!TrinketItem.equipItem(mob, gadgetStack)) continue;

                totalThreat += threat;

            }
        });
    }
}
