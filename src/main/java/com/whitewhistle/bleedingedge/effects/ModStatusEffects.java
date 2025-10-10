package com.whitewhistle.bleedingedge.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;


public class ModStatusEffects {
    //public static final StatusEffect STARFALL_EFFECT = registerStatusEffect("starfall_effect", new StarfallEffect(StatusEffectCategory.HARMFUL, 0x000000));

    // curses
    // public static final StatusEffect CURSE_OF_JITTERS = registerStatusEffect("curse_of_jitters", new CursedStatusEffect(StatusEffectCategory.HARMFUL, 0xFF0000));
    // public static final StatusEffect DARK_LEECH_TRIGGER_COOLDOWN = registerStatusEffect("dark_leech_trigger_cooldown", new CursedStatusEffect(StatusEffectCategory.HARMFUL, 0xFF0000));

    // ============= impl ==================

    private static StatusEffect registerStatusEffect(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, name), effect);
    }


    public static void registerEffects() {

    }
}
