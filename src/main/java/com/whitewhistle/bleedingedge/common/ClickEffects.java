package com.whitewhistle.bleedingedge.common;

import net.minecraft.item.ItemStack;

public abstract class ClickEffects {

    // default instance should NOOP as it is run on the server
    public static ClickEffects INSTANCE = new ClickEffects() {

        @Override
        public void showToggleParticles(ItemStack stack, boolean enabled) {

        }
    };

    public abstract void showToggleParticles(ItemStack stack, boolean enabled);

}
