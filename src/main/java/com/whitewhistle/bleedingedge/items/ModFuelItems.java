package com.whitewhistle.bleedingedge.items;

import net.fabricmc.fabric.api.registry.FuelRegistry;

public class ModFuelItems {
    public static void init() {
        var r = FuelRegistry.INSTANCE;

        r.add(ModItems.HEATING_SLOT, 1);
    }
}
