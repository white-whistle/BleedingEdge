package com.whitewhistle.bleedingedge.hooks;

import com.whitewhistle.bleedingedge.ModKeyBindings;
import com.whitewhistle.bleedingedge.ability.IHasAbilities;
import com.whitewhistle.bleedingedge.common.CommonBridge;
import com.whitewhistle.bleedingedge.gui.particles.GuiParticleRenderer;
import com.whitewhistle.bleedingedge.gui.particles.impl.ArrowParticle;
import com.whitewhistle.bleedingedge.gui.particles.impl.ThinkParticle;
import com.whitewhistle.bleedingedge.util.Interator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ClientBridge extends CommonBridge {
    @Override
    public void showToggleParticles(ItemStack stack, boolean enabled) {
        Interator.of(10).forEach(i -> {
            GuiParticleRenderer.addParticle(
                    new ArrowParticle(GuiParticleRenderer.getMousePos(), enabled ? ArrowParticle.GREEN : ArrowParticle.RED, enabled ? -1 : 1)
                            .randomizeVelocity(2.5f)
            );
        });
    }

    @Override
    public void showBrainThinkParticles(ItemStack stack, int amount) {
        Interator.of(amount).forEach(i -> {
            GuiParticleRenderer.addParticle(
                    new ThinkParticle(GuiParticleRenderer.getStackPosition(stack))
                            .randomizeVelocity(1f)
            );
        });
    }

    @Override
    public PlayerEntity getClientPlayer() {
        var client = MinecraftClient.getInstance();
        if (client == null) return null;

        return client.player;
    }

    @Override
    public void addHotkeyTooltip(ItemStack stack, List<Text> tooltip) {
        var item = stack.getItem();
        if (item instanceof IHasAbilities iHasAbilities) {
            var abilities = iHasAbilities.getAbilities();

            abilities.forEach(ability -> {
                tooltip.add(Text.translatable("tooltip.bleeding-edge.ability_hotkey.description", ability.getName(), ModKeyBindings.getAbilityKeyBind(item, ability).getBoundKeyLocalizedText().copy().styled(s -> s.withColor(Formatting.BLUE))).styled(s -> s.withColor(Formatting.GRAY)));
            });
        }
    }

    public static void register() {
        CommonBridge.INSTANCE = new ClientBridge();
    }
}
