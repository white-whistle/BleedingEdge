package com.whitewhistle.bleedingedge.gui.particles;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.Random;

public abstract class GuiParticle implements ParticleSystem.Particle {

    public static Random random = new Random();

    public abstract void render(Screen screen, DrawContext drawContext, int mouseX, int mouseY, float tickDelta);
}
