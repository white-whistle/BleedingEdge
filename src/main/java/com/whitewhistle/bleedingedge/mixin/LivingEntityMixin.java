package com.whitewhistle.bleedingedge.mixin;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import com.whitewhistle.bleedingedge.items.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
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

    @Inject(method="damage", at = @At(value = "RETURN"))
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        var livingEntity = (LivingEntity) (Object) this;

        if (livingEntity.getAttributeValue(ModEntityAttributes.SHIELD) > 0) {
            var damaged = cir.getReturnValue();
            if (damaged) {
                var duration = 20 * 5;
                livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SHIELD_COOLDOWN, duration, 0, true, false, false));
                if (livingEntity instanceof PlayerEntity player) {
                    player.getItemCooldownManager().set(ModItems.SHIELD_GENERATOR, duration);
                }
            }
        }
    }

}
