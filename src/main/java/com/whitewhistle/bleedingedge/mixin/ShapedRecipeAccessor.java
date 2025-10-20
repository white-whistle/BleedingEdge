package com.whitewhistle.bleedingedge.mixin;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {

    // @Accessor
    // int getWidth();


    // @Accessor
    // int getHeight();

    // @Accessor
    // String getGroup();

    // @Accessor
    // CraftingRecipeCategory getCategory();

    @Accessor
    DefaultedList<Ingredient> getInput();

    @Accessor
    ItemStack getOutput();

    @Accessor
    boolean getShowNotification();

    @Invoker
    static DefaultedList<Ingredient> callCreatePatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
        return null;
    }

    @Invoker
    static Map<String, Ingredient> callReadSymbols(JsonObject json) {
        return null;
    }

    @Invoker
    static String[] callRemovePadding(String... pattern) {
        return null;
    }

}
