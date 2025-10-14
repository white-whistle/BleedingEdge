package com.whitewhistle.bleedingedge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.whitewhistle.bleedingedge.ModItemRenderer;
import com.whitewhistle.bleedingedge.items.impl.SlotItem;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    // /** @noinspection MixinAnnotationTarget*/
    // @WrapMethod(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V")
    // void renderItemProxy(LivingEntity entity,
    //                      ItemStack stack,
    //                      ModelTransformationMode renderMode,
    //                      boolean leftHanded,
    //                      MatrixStack matrices,
    //                      VertexConsumerProvider vertexConsumers,
    //                      World world,
    //                      int light,
    //                      int overlay,
    //                      int seed,
    //                      Operation<Void> original) {
    //
    //     // usually used for in world renders (hand, item frame, etc..)
    //     ModItemRenderer.renderItem(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, world, light, overlay, seed, original);
    // }

}
