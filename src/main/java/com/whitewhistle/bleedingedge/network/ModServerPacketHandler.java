package com.whitewhistle.bleedingedge.network;

import com.whitewhistle.bleedingedge.items.IHasHotkeys;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModServerPacketHandler {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ModPackets.ITEM_TRIGGER, ((server, player, handler, buf, responseSender) -> {
            var triggerStack = buf.readItemStack();
            var action = buf.readIdentifier();
            if (triggerStack.isEmpty()) return;

            var triggerItem = triggerStack.getItem();
            if (!(triggerItem instanceof IHasHotkeys iHasHotkeys)) return;

            var trinketComponent = TrinketsApi.getTrinketComponent(player);

            trinketComponent.ifPresent(trinkets -> {
                var equippedStacks = trinkets.getEquipped(triggerItem);

                equippedStacks.forEach(pair -> {
                    var stack = pair.getRight();

                    iHasHotkeys.handleHotkey(action, player, stack);
                });
            });
        }));
    }
}
