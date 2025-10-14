package com.whitewhistle.bleedingedge;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.whitewhistle.bleedingedge.items.impl.SlotItem;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ModItemRenderer {
    public static void renderItem(LivingEntity entity,
                                  ItemStack stack,
                                  ModelTransformationMode renderMode,
                                  boolean leftHanded,
                                  MatrixStack matrices,
                                  VertexConsumerProvider vertexConsumers,
                                  World world,
                                  int light,
                                  int overlay,
                                  int seed,
                                  Operation<Void> original) {

        var item = stack.getItem();

        if (item instanceof SlotItem && ModComponents.PROCESSING_STACK.has(stack)) {
            var processingStack = ModComponents.PROCESSING_STACK.get(stack);

            // draw slot
            original.call(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, world, light, overlay, seed);

            // draw slot contents
            matrices.push();
            matrices.translate(0, 0, -1 / 32f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            original.call(entity, processingStack, renderMode, leftHanded, matrices, vertexConsumers, world, light, overlay, seed);
            matrices.pop();

            return;
        }

        original.call(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, world, light, overlay, seed);

    }

    public static void drawItem(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, Operation<Void> original, MatrixStack matrices) {
        var item = stack.getItem();

        if (item instanceof SlotItem && ModComponents.PROCESSING_STACK.has(stack)) {
            var processingStack = ModComponents.PROCESSING_STACK.get(stack);

            // draw slot
            original.call(entity, world, stack, x, y, seed, z);

            var scale = 0.5f;

            // draw slot contents
            matrices.push();
            matrices.translate(x, y, z + 1);
            matrices.scale(scale, scale, 1);
            original.call(entity, world, processingStack, 8, 8, seed, 0);
            matrices.pop();



            return;
        }

        original.call(entity, world, stack, x, y, seed, z);
    }
}
