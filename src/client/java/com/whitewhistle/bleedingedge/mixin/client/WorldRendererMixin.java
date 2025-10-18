package com.whitewhistle.bleedingedge.mixin.client;

import com.whitewhistle.bleedingedge.render.ThreatOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "renderEntity", at = @At(value = "RETURN"))
    private void renderEntity(Entity entity, double x, double y, double z, float g, MatrixStack matrix, VertexConsumerProvider v, CallbackInfo info) {
        if (entity instanceof LivingEntity && !entity.isInvisible()) {
            ThreatOverlayRenderer.prepareRenderInWorld((LivingEntity) entity);
        }
    }
}
