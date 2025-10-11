package com.whitewhistle.bleedingedge.items;

import com.whitewhistle.bleedingedge.common.ClickEffects;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.item.ItemStack;

public interface ToggledItem {

    default boolean toggle(ItemStack itemStack) {
        var enabled = ModComponents.ENABLED.map(itemStack, (prev) -> !prev);

        ClickEffects.INSTANCE.showToggleParticles(itemStack, enabled);

        return enabled;
    }

    default boolean isEnabled(ItemStack stack) {
        return ModComponents.ENABLED.get(stack);
    }
}
