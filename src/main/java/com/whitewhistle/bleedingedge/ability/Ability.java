package com.whitewhistle.bleedingedge.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Ability {
    public final Identifier id;

    public Ability(Identifier id) {
        this.id = id;
    }

    public Text getName() {
        return Text.translatable("ability." + id.getNamespace() + "." + id.getPath() + ".name");
    }

    public void trigger(PlayerEntity player, ItemStack stack) {

    }

    public boolean isOf(Identifier identifier) {
        return this.id.equals(identifier);
    }
}
