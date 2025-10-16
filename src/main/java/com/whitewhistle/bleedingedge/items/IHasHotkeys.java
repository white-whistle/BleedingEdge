package com.whitewhistle.bleedingedge.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface IHasHotkeys {

    void handleHotkey(Identifier packetId, PlayerEntity player, ItemStack stack);

}
