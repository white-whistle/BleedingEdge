package com.whitewhistle.bleedingedge.mixin;

import com.whitewhistle.bleedingedge.items.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        var enc = (Enchantment)(Object)(this);

        if (stack.isOf(ModItems.BREACH_HAMMER) && enc == Enchantments.QUICK_CHARGE) {
            cir.setReturnValue(true);
            return;
        }
    }
}
