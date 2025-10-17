package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.common.CommonBridge;
import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThreatVisorItem extends ModTrinketItem implements ElectricToggledItem.WithAbilities {
    public ThreatVisorItem(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);

        if (this.isEnabled(entity, stack)) {
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.THREAT_VISION, 20, 0, true, false, false));
        } else {
            entity.removeStatusEffect(ModStatusEffects.THREAT_VISION);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);


        tooltip.add(Text.translatable("tooltip.bleeding-edge.threat_visor.description").styled(s -> s.withColor(Formatting.GRAY)));

        var player = CommonBridge.INSTANCE.getClientPlayer();
        if (player != null) {
            tooltip.add(Text.translatable("tooltip.bleeding-edge.threat_visor.description.own_threat", player.getAttributeValue(ModEntityAttributes.THREAT)).styled(s -> s.withColor(Formatting.GRAY)));
        }

        tooltip.add(Text.empty());

    }
}
