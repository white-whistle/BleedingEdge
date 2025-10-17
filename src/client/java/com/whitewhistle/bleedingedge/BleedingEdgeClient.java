package com.whitewhistle.bleedingedge;

import com.whitewhistle.bleedingedge.gui.particles.GuiParticleRenderer;
import com.whitewhistle.bleedingedge.hooks.ClientBridge;
import com.whitewhistle.bleedingedge.network.ModClientPacketHandler;
import com.whitewhistle.bleedingedge.render.ModTrinketRenderer;
import com.whitewhistle.bleedingedge.shaders.ModShaders;
import com.whitewhistle.bleedingedge.tooltip.ModTooltipComponents;
import net.fabricmc.api.ClientModInitializer;

public class BleedingEdgeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		GuiParticleRenderer.INSTANCE.init();
		ClientBridge.register();
		ModShaders.register();
		ModModelPredicateProvider.registerPredicates();
		ModKeyBindings.init();
		ModTrinketRenderer.init();
		ModClientPacketHandler.register();
		ModTooltipComponents.register();
	}
}