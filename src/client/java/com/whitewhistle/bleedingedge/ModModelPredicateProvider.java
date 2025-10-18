package com.whitewhistle.bleedingedge;

import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {

    private static final ClampedModelPredicateProvider PULLING_PREDICATE = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
    private static final ClampedModelPredicateProvider PULL_PREDICATE = (stack, world, entity, seed) -> {
        if (entity == null) {
            return 0.0F;
        } else {
            return entity.getActiveItem() != stack ? 0.0F : (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
        }
    };

    private static void registerCustomBow(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("pull"), PULL_PREDICATE);
        ModelPredicateProviderRegistry.register(
                item,
                new Identifier("pulling"),
                PULLING_PREDICATE
        );
    }

    public static void registerPredicates() {

        var chargedPredicate = ModIdentifier.of("charged");

        ModelPredicateProviderRegistry.register(ModItems.BREACH_HAMMER, chargedPredicate, (stack, world, entity, seed) -> ModComponents.CHARGED.get(stack) ? 1 : 0);

        registerCustomBow(ModItems.STORM_BENDER);

    }
}
