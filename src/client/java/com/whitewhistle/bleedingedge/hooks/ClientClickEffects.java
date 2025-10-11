package com.whitewhistle.bleedingedge.hooks;

import com.whitewhistle.bleedingedge.common.ClickEffects;
import com.whitewhistle.bleedingedge.gui.particles.GuiParticleRenderer;
import com.whitewhistle.bleedingedge.gui.particles.impl.ArrowParticle;
import com.whitewhistle.bleedingedge.util.Interator;
import net.minecraft.item.ItemStack;

public class ClientClickEffects extends ClickEffects {
    @Override
    public void showToggleParticles(ItemStack stack, boolean enabled) {
        Interator.of(10).forEach(i -> {
            GuiParticleRenderer.addParticle(
                    new ArrowParticle(GuiParticleRenderer.getMousePos(), enabled ? ArrowParticle.GREEN : ArrowParticle.RED, enabled ? -1 : 1)
                            .randomizeVelocity(2.5f)
            );
        });
    }

    public static void register() {
        ClickEffects.INSTANCE = new ClientClickEffects();
    }
}
