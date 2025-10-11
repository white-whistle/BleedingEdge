package com.whitewhistle.bleedingedge.effects;

import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import com.whitewhistle.bleedingedge.util.UuidUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.UUID;
import java.util.function.Function;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;


public class ModStatusEffects {
    public static final StatusEffect CLOAKED = registerStatusEffect("cloaked", new AttributeStatusEffect(UuidUtil.from("cloaked"),StatusEffectCategory.BENEFICIAL, 0x000000, ModEntityAttributes.CLOAKING, i -> i+1));

    // ============= impl ==================

    public static class ModStatusEffect extends StatusEffect {
        protected ModStatusEffect(StatusEffectCategory category, int color) {
            super(category, color);
        }
    }

    public static class AttributeStatusEffect extends ModStatusEffect {

        private final UUID modifierId;
        private final EntityAttribute attribute;
        private final Function<Integer, Integer> scaling;
        protected AttributeStatusEffect(UUID modifierId, StatusEffectCategory category, int color, EntityAttribute attribute, Function<Integer, Integer> scaling) {
            super(category, color);
            this.modifierId = modifierId;
            this.attribute = attribute;
            this.scaling = scaling;
        }

        @Override
        public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
            super.onApplied(entity, attributes, amplifier);

            var instance = entity.getAttributeInstance(this.attribute);
            if (instance != null) {
                instance.removeModifier(modifierId);
                instance.addPersistentModifier(
                        new EntityAttributeModifier(this.modifierId, "My custom effect", this.scaling.apply(amplifier), EntityAttributeModifier.Operation.ADDITION)
                );
            }
        }

        @Override
        public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
            super.onRemoved(entity, attributes, amplifier);
            var instance = entity.getAttributeInstance(this.attribute);
            if (instance != null) {
                instance.removeModifier(modifierId);
            }

        }
    }

    private static StatusEffect registerStatusEffect(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, name), effect);
    }

    public static void registerEffects() {

    }
}
