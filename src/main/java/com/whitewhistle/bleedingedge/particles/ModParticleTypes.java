package com.whitewhistle.bleedingedge.particles;

import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticleTypes {
    public static final DefaultParticleType ELECTRICITY = registerParticle("electricity", FabricParticleTypes.simple());
    public static final DefaultParticleType STORM_BEAM = registerParticle("storm_beam", FabricParticleTypes.simple(true));

    private static <T extends ParticleType<?>> T registerParticle(String name, T particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, ModIdentifier.of(name), particleType);
    }

    public static void register() {
    }
}
