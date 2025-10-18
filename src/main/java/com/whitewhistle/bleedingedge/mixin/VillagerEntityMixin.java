package com.whitewhistle.bleedingedge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.whitewhistle.bleedingedge.items.ModItems;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {

    @Shadow
    protected abstract void sayNo();

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void sayNoToBadItems(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        var stack = player.getStackInHand(hand);

        var villager = (VillagerEntity) (Object) this;

        if (stack.isOf(ModItems.BRAIN_DRILL) && ! villager.hasStatusEffect(StatusEffects.WEAKNESS)) {
            sayNo();
            cir.setReturnValue(ActionResult.success(player.getWorld().isClient));
        }
    }

    @WrapOperation(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean isItemAllowedToClickOnVillager(ItemStack stack, Item item, Operation<Boolean> original) {
        var villager = (VillagerEntity) (Object) this;
        if (villager.hasStatusEffect(StatusEffects.WEAKNESS)) {
            return original.call(stack, ModItems.BRAIN_DRILL);
        }

        return original.call(stack, item);
    }
}
