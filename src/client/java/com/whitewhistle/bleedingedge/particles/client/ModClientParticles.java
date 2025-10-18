package com.whitewhistle.bleedingedge.particles.client;

import com.whitewhistle.bleedingedge.particles.ModParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModClientParticles {
    public static void register() {

        ParticleFactoryRegistry.getInstance().register(ModParticles.ELECTRICITY, ElectricityParticle.Factory::new);

    }
}
