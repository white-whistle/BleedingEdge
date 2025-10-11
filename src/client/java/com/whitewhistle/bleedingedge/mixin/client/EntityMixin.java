package com.whitewhistle.bleedingedge.mixin.client;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method="isInvisible", at = @At("HEAD"), cancellable = true)
    private void isInvisible(CallbackInfoReturnable<Boolean> cir) {
        var client = MinecraftClient.getInstance();
        if (client == null) return;

        var player = client.player;
        if (player == null) return;

        if (player.hasStatusEffect(ModStatusEffects.IR_VISION)) {
            cir.setReturnValue(false);
        }
    }
}
