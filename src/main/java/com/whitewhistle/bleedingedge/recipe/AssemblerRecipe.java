package com.whitewhistle.bleedingedge.recipe;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.whitewhistle.bleedingedge.mixin.ShapedRecipeAccessor;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class AssemblerRecipe extends ShapedRecipe {
    public static class Type implements net.minecraft.recipe.RecipeType<AssemblerRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "assembler_recipe";
    }

    public AssemblerRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output, boolean showNotification) {
        super(id, group, category, width, height, input, output, showNotification);
    }

    public AssemblerRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
        super(id, group, category, width, height, input, output);
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    static String[] getPattern(JsonArray json) {
        String[] strings = new String[json.size()];
        if (strings.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < strings.length; i++) {
                String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");

                if (i > 0 && strings[0].length() != string.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                strings[i] = string;
            }

            return strings;
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<AssemblerRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        // This will be the "type" field in the json
        public static final Identifier ID = ModIdentifier.of("assembler_recipe");

        public AssemblerRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            CraftingRecipeCategory craftingRecipeCategory = (CraftingRecipeCategory)CraftingRecipeCategory.CODEC
                    .byId(JsonHelper.getString(jsonObject, "category", null), CraftingRecipeCategory.MISC);
            Map<String, Ingredient> map = ShapedRecipeAccessor.callReadSymbols(JsonHelper.getObject(jsonObject, "key"));
            String[] strings = ShapedRecipeAccessor.callRemovePadding(getPattern(JsonHelper.getArray(jsonObject, "pattern")));
            int i = strings[0].length();
            int j = strings.length;
            DefaultedList<Ingredient> defaultedList = ShapedRecipeAccessor.callCreatePatternMatrix(strings, map, i, j);
            ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            boolean bl = JsonHelper.getBoolean(jsonObject, "show_notification", true);
            return new AssemblerRecipe(identifier, string, craftingRecipeCategory, i, j, defaultedList, itemStack, bl);
        }

        public AssemblerRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            int j = packetByteBuf.readVarInt();
            String string = packetByteBuf.readString();
            CraftingRecipeCategory craftingRecipeCategory = packetByteBuf.readEnumConstant(CraftingRecipeCategory.class);
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < defaultedList.size(); k++) {
                defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            boolean bl = packetByteBuf.readBoolean();
            return new AssemblerRecipe(identifier, string, craftingRecipeCategory, i, j, defaultedList, itemStack, bl);
        }

        public void write(PacketByteBuf packetByteBuf, AssemblerRecipe shapedRecipe) {
            var acc = (ShapedRecipeAccessor)shapedRecipe;
            packetByteBuf.writeVarInt(shapedRecipe.getWidth());
            packetByteBuf.writeVarInt(shapedRecipe.getHeight());
            packetByteBuf.writeString(shapedRecipe.getGroup());
            packetByteBuf.writeEnumConstant(shapedRecipe.getCategory());

            for (Ingredient ingredient : acc.getInput()) {
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(acc.getOutput());
            packetByteBuf.writeBoolean(acc.getShowNotification());
        }
    }
}
