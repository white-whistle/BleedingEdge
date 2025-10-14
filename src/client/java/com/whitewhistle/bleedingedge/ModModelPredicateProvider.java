package com.whitewhistle.bleedingedge;

import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.client.item.ModelPredicateProviderRegistry;

public class ModModelPredicateProvider {

    public static void registerPredicates() {

        var chargedPredicate = ModIdentifier.of("charged");

        ModelPredicateProviderRegistry.register(ModItems.BREACH_HAMMER, chargedPredicate, (stack, world, entity, seed) -> ModComponents.CHARGED.get(stack) ? 1 : 0);

    }
}
