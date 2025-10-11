package com.whitewhistle.bleedingedge;

import com.whitewhistle.bleedingedge.gui.particles.GuiParticleRenderer;
import com.whitewhistle.bleedingedge.hooks.ClientClickEffects;
import net.fabricmc.api.ClientModInitializer;

public class BleedingEdgeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		GuiParticleRenderer.INSTANCE.init();
		ClientClickEffects.register();
	}
}