package com.whitewhistle.bleedingedge.mixin.client;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            var client = MinecraftClient.getInstance();
            if (client == null) return;

            var player = client.player;
            if (player == null) return;

            if (player.hasStatusEffect(ModStatusEffects.IR_VISION)) return;

            var cloakingLevel = livingEntity.getAttributeValue(ModEntityAttributes.CLOAKING);

            if (cloakingLevel > 0) {
                ci.cancel();
            }
        }
    }

}
