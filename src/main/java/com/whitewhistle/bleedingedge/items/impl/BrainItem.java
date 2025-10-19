package com.whitewhistle.bleedingedge.items.impl;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.util.UuidUtil;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BrainItem extends Item {
    public final int brainPower;

    public static final UUID STEEL_BRAIN_CORE_SLOT_UUID = UuidUtil.from("steel_brain_consumed");
    public static final Multimap<String, EntityAttributeModifier> STEEL_BRAIN_MODIFIERS = new ImmutableMultimap.Builder<String, EntityAttributeModifier>().put("head/core", new EntityAttributeModifier(STEEL_BRAIN_CORE_SLOT_UUID,"steel brain consumed", 1, EntityAttributeModifier.Operation.ADDITION)).build();

    public BrainItem(Settings settings, int brainPower) {
        super(settings);
        this.brainPower = brainPower;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (stack.isOf(ModItems.STEEL_BRAIN)) {
            TrinketsApi.getTrinketComponent(user).ifPresent(trinketComponent -> {
                trinketComponent.addPersistentModifiers(STEEL_BRAIN_MODIFIERS);
            });
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (stack.isOf(ModItems.STEEL_BRAIN)) {
            tooltip.add(Text.translatable("tooltip.bleeding-edge.steel_brain.description").styled(s -> s.withColor(Formatting.GRAY)));
        }
    }
}
