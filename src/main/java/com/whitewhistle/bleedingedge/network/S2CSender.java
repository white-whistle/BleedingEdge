package com.whitewhistle.bleedingedge.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class S2CSender {
    public static void sendKevlarTotemTrigger(ServerPlayerEntity serverPlayer) {
        ServerPlayNetworking.send(serverPlayer, ModPackets.KEVLAR_TOTEM_TRIGGER, PacketByteBufs.empty());
    }
}
