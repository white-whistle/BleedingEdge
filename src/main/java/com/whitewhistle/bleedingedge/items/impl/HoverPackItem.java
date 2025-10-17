package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import dev.emi.trinkets.api.SlotReference;
import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class HoverPackItem extends ModTrinketItem implements ElectricToggledItem.WithAbilities {

    public static final AbilitySource HOVER_PACK_FLIGHT = Pal.getAbilitySource(MOD_ID, "ends_bounty_flight");

    public HoverPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getThreatLevel() {
        return ModTrinketItem.MAJOR_THREAT;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        if (!entity.getWorld().isClient && entity instanceof PlayerEntity player) {
            var grant = HOVER_PACK_FLIGHT.grants(player, VanillaAbilities.ALLOW_FLYING);
            var enabled = isEnabled(entity, stack);

            if (grant && !enabled) {
                HOVER_PACK_FLIGHT.revokeFrom(player, VanillaAbilities.ALLOW_FLYING);
            } else if (enabled && !grant) {
                HOVER_PACK_FLIGHT.grantTo(player, VanillaAbilities.ALLOW_FLYING);
            }
        }
    }
}
