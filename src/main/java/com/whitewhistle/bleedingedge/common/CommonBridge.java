package com.whitewhistle.bleedingedge.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public abstract class CommonBridge {

    // default instance should NOOP as it is run on the server
    public static CommonBridge INSTANCE = new CommonBridge() {

        @Override
        public void showToggleParticles(ItemStack stack, boolean enabled) {

        }

        @Override
        public void showBrainThinkParticles(ItemStack stack, int amount) {

        }

        @Override
        public PlayerEntity getClientPlayer() {
            return null;
        }

        @Override
        public void addHotkeyTooltip(ItemStack stack, List<Text> tooltip) {

        }
    };

    public abstract void showToggleParticles(ItemStack stack, boolean enabled);
    public abstract void showBrainThinkParticles(ItemStack stack, int amount);
    public abstract PlayerEntity getClientPlayer();
    public abstract void addHotkeyTooltip(ItemStack stack, List<Text> tooltip);

}
