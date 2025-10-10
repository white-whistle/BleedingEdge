package com.whitewhistle.bleedingedge.mixin;

import com.whitewhistle.bleedingedge.items.GlobalItemClick;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(at = @At("HEAD"), method = "onClicked", cancellable = true)
    private void onClicked(ItemStack _stack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        var stack = (ItemStack) (Object) this;
        var optionalResult = GlobalItemClick.onClicked(stack, slot, clickType, player, cursorStackReference);

        optionalResult.ifPresent(cir::setReturnValue);
    }

    @Inject(at = @At("RETURN"), method = "getTooltip")
    private void addHexingTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        if (player == null) return;

        var stack = (ItemStack)(Object)this;
        var tooltip = cir.getReturnValue();
        var item = stack.getItem();

        // generic append tooltip onto applied items
    }

}
