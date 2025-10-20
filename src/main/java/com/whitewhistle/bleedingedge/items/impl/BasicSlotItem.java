package com.whitewhistle.bleedingedge.items.impl;

import com.whitewhistle.bleedingedge.items.IHasModel;
import net.minecraft.data.client.Model;

public class BasicSlotItem extends SlotItem implements IHasModel {
    public BasicSlotItem(Settings settings) {
        super(settings);
    }

    @Override
    public Model getBaseModel() {
        return null;
    }
}
