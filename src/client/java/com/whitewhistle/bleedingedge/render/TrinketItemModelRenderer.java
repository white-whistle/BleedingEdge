package com.whitewhistle.bleedingedge.render;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class TrinketItemModelRenderer implements TrinketRenderer {

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity instanceof AbstractClientPlayerEntity player && contextModel instanceof PlayerEntityModel<?>) {

            var playerModel = (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel;

            matrices.push();

            switch (slotReference.inventory().getSlotType().getName()) {
                case "face" -> {
                    TrinketRenderer.translateToFace(matrices, playerModel, player, headYaw, headPitch);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                }
                case "back" -> {
                    TrinketRenderer.translateToChest(matrices, playerModel, player);
                    matrices.translate(0, -0.2f, 0.4f);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                }
                case "belt" -> {
                    TrinketRenderer.translateToChest(matrices, playerModel, player);
                    matrices.translate(0, 0.3f, 0);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                }
                case "necklace" -> {
                    TrinketRenderer.translateToChest(matrices, playerModel, player);
                    matrices.translate(0, -0.1f, 0);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                }
            }

            matrices.scale(0.6f,0.6f,1f);

            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, null, 0);
            matrices.pop();
        } else if (contextModel instanceof BipedEntityModel<? extends LivingEntity> bipedEntityModel) {
            // generic biped entity render
            matrices.push();

            switch (slotReference.inventory().getSlotType().getName()) {
                case "face" -> {
                    translateToBipedFace(matrices, bipedEntityModel, headYaw, headPitch);

                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                }
                case "back" -> {
                    translateToBipedChest(matrices, bipedEntityModel);

                    matrices.translate(0, -0.2f, 0.4f);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                }
                case "belt" -> {
                    translateToBipedChest(matrices, bipedEntityModel);

                    matrices.translate(0, 0.3f, 0);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                }
                case "necklace" -> {
                    translateToBipedChest(matrices, bipedEntityModel);

                    matrices.translate(0, -0.1f, 0);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                }
            }

            matrices.scale(0.6f,0.6f,1f);

            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, null, 0);
            matrices.pop();
        }

    }

    private void translateToBipedChest(MatrixStack matrices, BipedEntityModel<? extends LivingEntity> bipedEntityModel) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(bipedEntityModel.body.yaw));
        matrices.translate(0.0F, 0.4F, -0.16F);
    }
    private void translateToBipedFace(MatrixStack matrices, BipedEntityModel<? extends LivingEntity> bipedEntityModel, float headYaw, float headPitch) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(headYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(headPitch));

		matrices.translate(0.0F, -0.25F, -0.3F);
    }

}
