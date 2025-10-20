package com.whitewhistle.bleedingedge.recipe;

import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipeTypes {
    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, AssemblerRecipe.Serializer.ID,
                AssemblerRecipe.Serializer.INSTANCE);

        Registry.register(Registries.RECIPE_TYPE, ModIdentifier.of(AssemblerRecipe.Type.ID), AssemblerRecipe.Type.INSTANCE);
    }
}
