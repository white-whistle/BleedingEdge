package com.whitewhistle.bleedingedge.ability;

import com.whitewhistle.bleedingedge.items.IHasHotkeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

public interface IHasAbilities extends IHasHotkeys {

    List<Ability> getAbilities();

    default void handleHotkey(Identifier packetId, PlayerEntity player, ItemStack stack) {
        var abilities = this.getAbilities();

        for (var ability: abilities) {
            if (ability.isOf(packetId)) {
                ability.trigger(player, stack);
                return;
            }
        }
    }

}
