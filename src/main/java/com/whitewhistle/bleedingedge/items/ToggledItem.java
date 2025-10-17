package com.whitewhistle.bleedingedge.items;

import com.whitewhistle.bleedingedge.ability.Ability;
import com.whitewhistle.bleedingedge.common.CommonBridge;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

import static com.whitewhistle.bleedingedge.util.RandomUtil.r0;

public interface ToggledItem {

    Ability TOGGLE_ABILITY = new Ability(ModIdentifier.of("toggle")) {
        @Override
        public void trigger(PlayerEntity player, ItemStack stack) {
            super.trigger(player, stack);

            if (stack.getItem() instanceof ToggledItem toggledItem) {
                var enabled = toggledItem.toggle(stack);

                toggledItem.playToggleSound(player, enabled);
            }
        }
    };

    default boolean toggle(ItemStack itemStack) {
        var enabled = ModComponents.ENABLED.map(itemStack, (prev) -> !prev);

        CommonBridge.INSTANCE.showToggleParticles(itemStack, enabled);

        return enabled;
    }

    default void playToggleSound(PlayerEntity player, boolean enabled) {
        if (enabled) {
            player.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, r0(), r0());
        } else {
            player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, r0(), r0());
        }
    }

    default boolean isEnabled(LivingEntity livingEntity, ItemStack stack) {
        return ModComponents.ENABLED.get(stack);
    }
}
