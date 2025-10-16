package com.whitewhistle.bleedingedge.network;

import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class C2SPacketSender {
    public static void triggerItem(Item item){
        triggerItem(item, ModIdentifier.EMPTY);
    }

    public static void triggerItem(Item item, Identifier action){
        var buf = PacketByteBufs.create();

        buf.writeItemStack(item.getDefaultStack());
        buf.writeIdentifier(action);

        ClientPlayNetworking.send(ModPackets.ITEM_TRIGGER, buf);
    }
}
