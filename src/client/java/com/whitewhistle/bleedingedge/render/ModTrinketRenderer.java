package com.whitewhistle.bleedingedge.render;

import com.whitewhistle.bleedingedge.items.ModItems;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;

public class ModTrinketRenderer {

    public static void init() {
        var trinketItemModelRenderer = new TrinketItemModelRenderer();

        ModItems.modItems.forEach(item -> {
            if (item instanceof Trinket) {
                TrinketRendererRegistry.registerRenderer(item, trinketItemModelRenderer);
            }
        });
    }

}
