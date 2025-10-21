package com.whitewhistle.bleedingedge.mixin;

import com.whitewhistle.bleedingedge.items.ModItems;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Unique
    private boolean wasFlying = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        var player = (PlayerEntity) (Object) (this);

        TrinketsApi.getTrinketComponent(player).ifPresent(comp -> {
            if (comp.isEquipped(ModItems.HOVER_PACK)) {
                var currentFlying = player.getAbilities().flying;
                if (currentFlying != wasFlying) {
                    player.playSound(currentFlying ? SoundEvents.ENTITY_ENDER_EYE_DEATH : SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
                    wasFlying = currentFlying;
                }

            }
        });
    }

}
