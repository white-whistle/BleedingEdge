package com.whitewhistle.bleedingedge.mixin;

import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "createLivingAttributes", require = 1, allow = 1, at = @At("RETURN"))
    private static void addAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        var builder = cir.getReturnValue();

        ModEntityAttributes.LIVING_ENTITY_ADDITIONAL_ATTRIBUTES.forEach(builder::add);
    }

}
