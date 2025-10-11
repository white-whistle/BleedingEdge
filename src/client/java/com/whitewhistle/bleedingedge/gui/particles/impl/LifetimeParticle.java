package com.whitewhistle.bleedingedge.gui.particles.impl;

import com.whitewhistle.bleedingedge.gui.particles.GuiParticle;

public abstract class LifetimeParticle extends GuiParticle {
    int lifetime;
    int currentLifetime;


    public LifetimeParticle(int lifetime) {
        this.lifetime = lifetime;
        this.currentLifetime = lifetime;
    }

    @Override
    public void tick() {
        currentLifetime -= 1;
    }

    @Override
    public boolean isDead() {
        return this.currentLifetime <= 0;
    }

    public float getProgress() {
        return 1 - (Math.max(0, this.currentLifetime) / (float) this.lifetime);
    }
}