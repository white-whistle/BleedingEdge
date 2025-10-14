package com.whitewhistle.bleedingedge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.whitewhistle.bleedingedge.ModItemRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Shadow @Final private MatrixStack matrices;

    /** @noinspection MixinAnnotationTarget*/
    @WrapMethod(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V")
    private void renderItemProxy( LivingEntity entity,  World world, ItemStack stack, int x, int y, int seed, int z, Operation<Void> original) {
        ModItemRenderer.drawItem(entity, world, stack, x, y, seed, z, original, matrices);
    }
}
