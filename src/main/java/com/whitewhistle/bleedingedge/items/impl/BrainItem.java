package com.whitewhistle.bleedingedge.items.impl;

import net.minecraft.item.Item;

public class BrainItem extends Item {
    public final int brainPower;

    public BrainItem(Settings settings, int brainPower) {
        super(settings);
        this.brainPower = brainPower;
    }
}
