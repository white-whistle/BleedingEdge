package com.whitewhistle.bleedingedge;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.ModItemGroups;
import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BleedingEdge implements ModInitializer {
    public static final String MOD_ID = "bleeding-edge";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModComponents.register();
        ModStatusEffects.registerEffects();
        ModItemGroups.registerGroups();

        LOGGER.info("++Blessing from the gun god++");
    }
}