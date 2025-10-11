package com.whitewhistle.bleedingedge.nbt;

import net.minecraft.util.Identifier;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class ModComponents {

    public static final ItemComponent<Boolean> ENABLED = new ItemComponent<>(new Identifier(MOD_ID, "enabled"), ModCodecs.BOOLEAN);

    public static void register() {

    }
}
