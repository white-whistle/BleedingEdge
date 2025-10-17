package com.whitewhistle.bleedingedge.network;

import com.whitewhistle.bleedingedge.ability.Ability;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.Item;

public class C2SPacketSender {
    public static void triggerAbility(Item item, Ability ability){
        var buf = PacketByteBufs.create();

        buf.writeItemStack(item.getDefaultStack());
        buf.writeIdentifier(ability.id);

        ClientPlayNetworking.send(ModPackets.ITEM_TRIGGER, buf);
    }
}
