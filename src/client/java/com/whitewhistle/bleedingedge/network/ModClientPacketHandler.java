package com.whitewhistle.bleedingedge.network;

import com.whitewhistle.bleedingedge.items.ModItems;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

public class ModClientPacketHandler {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.KEVLAR_TOTEM_TRIGGER, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                var player = client.player;
                if (player == null) return;

                client.particleManager.addEmitter(player, ParticleTypes.TOTEM_OF_UNDYING, 30);
                player.getWorld().playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_TOTEM_USE, player.getSoundCategory(), 1.0F, 1.0F, false);

                client.gameRenderer.showFloatingItem(new ItemStack(ModItems.KEVLAR_TOTEM));
            });
        });
    }
}
