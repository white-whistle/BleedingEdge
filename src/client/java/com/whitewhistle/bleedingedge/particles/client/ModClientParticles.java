package com.whitewhistle.bleedingedge.particles.client;

import com.whitewhistle.bleedingedge.particles.ModParticleTypes;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModClientParticles {
    public static void register() {

        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.ELECTRICITY, ElectricityParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.STORM_BEAM, StormBeamParticle.Factory::new);

    }
}
