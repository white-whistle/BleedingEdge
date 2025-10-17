package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.whitewhistle.bleedingedge.util.RandomUtil.r0;

public class SupplyDropItem extends Item {
    public SupplyDropItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        var trinketComponent = TrinketsApi.getTrinketComponent(entity);

        if (!user.getWorld().isClient && trinketComponent.isPresent()) {
            var t = trinketComponent.get();

            var inventoryMap = t.getInventory();

            var cloakingDeviceStack = ModItems.CLOAKING_DEVICE.getDefaultStack();
            ModComponents.ENABLED.set(cloakingDeviceStack, true);
            cloakingDeviceStack.addEnchantment(Enchantments.VANISHING_CURSE, 1);

            inventoryMap.get("chest").get("back").setStack(0, cloakingDeviceStack);

            user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.PLAYERS, r0(), r0());

            return ActionResult.SUCCESS;
        }

        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable("tooltip.bleeding-edge.supply_drop.description1").styled(s -> s.withColor(Formatting.GRAY)));
        tooltip.add(Text.translatable("tooltip.bleeding-edge.supply_drop.description2").styled(s -> s.withColor(Formatting.GRAY)));
    }
}
