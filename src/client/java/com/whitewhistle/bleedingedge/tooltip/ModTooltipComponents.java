package com.whitewhistle.bleedingedge.tooltip;

import com.whitewhistle.bleedingedge.items.impl.SlotItem;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class ModTooltipComponents {

    public static void register() {
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof SlotItem.SlotItemTooltipData d) {
                return new SlotItemComponent(d);
            }

            return null;
        });
    }
}
