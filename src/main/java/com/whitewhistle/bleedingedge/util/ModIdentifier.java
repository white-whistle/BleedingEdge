package com.whitewhistle.bleedingedge.util;

import net.minecraft.util.Identifier;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class ModIdentifier {
    public static Identifier of(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static String string(String name) {
        return new Identifier(MOD_ID, name).toString();
    }

    public static Identifier EMPTY = of("empty");
}
