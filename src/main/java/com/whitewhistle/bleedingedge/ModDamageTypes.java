package com.whitewhistle.bleedingedge;

import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> BRAIN_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ModIdentifier.of("brain_damage"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }
}
