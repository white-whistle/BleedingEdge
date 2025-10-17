package com.whitewhistle.bleedingedge;

import com.whitewhistle.bleedingedge.ability.Ability;
import com.whitewhistle.bleedingedge.ability.IHasAbilities;
import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.items.ToggledItem;
import com.whitewhistle.bleedingedge.items.impl.ObsidianSashItem;
import com.whitewhistle.bleedingedge.network.C2SPacketSender;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class ModKeyBindings {
    private static final Map<Item, Map<Ability, KeyBinding>> itemAbilityBinds = new HashMap<>();
    private static final Map<Item, Map<Ability, Integer>> itemAbilityDefaultKeys = new HashMap<>();

    // public static final KeyBinding ACTIVATE_OBSIDIAN_SASH_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
    //         "key.bleeding-edge.activate_obsidian_sash", // The translation key of the keybinding's name
    //         InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
    //         GLFW.GLFW_KEY_G, // The keycode of the key
    //         "category.bleeding-edge.gadget_key_binding" // The translation key of the keybinding's category.
    // ));
    //
    // public static final KeyBinding TOGGLE_NIGHT_VISION_GOGGLES = KeyBindingHelper.registerKeyBinding(new KeyBinding(
    //         "key.bleeding-edge.toggle_night_vision_goggles", // The translation key of the keybinding's name
    //         InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
    //         GLFW.GLFW_KEY_V, // The keycode of the key
    //         "category.bleeding-edge.gadget_key_binding" // The translation key of the keybinding's category.
    // ));

    public static void registerDefaultBind(Item item, Ability ability, int key) {
        var abilityDefaultBinds = itemAbilityDefaultKeys.getOrDefault(item, new HashMap<>());
        abilityDefaultBinds.put(ability, key);
        itemAbilityDefaultKeys.put(item, abilityDefaultBinds);
    }

    public static void registerItemAbilities(Item item, String category) {
        if (item instanceof IHasAbilities iHasAbilities) {
            iHasAbilities.getAbilities().forEach(ability -> {
                var itemId = Registries.ITEM.getId(item);
                var modId = itemId.getNamespace();
                var keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key." + modId + "." + itemId.getPath() + "." + ability.id.getPath(), // The translation key of the keybinding's name
                        InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                        getAbilityDefaultKey(item, ability), // The keycode of the key
                        "category." + modId + "." + category // The translation key of the keybinding's category.
                ));

                var abilityBinds = itemAbilityBinds.getOrDefault(item, new HashMap<>());

                abilityBinds.put(ability, keyBinding);

                itemAbilityBinds.put(item, abilityBinds);

            });
        }
    }

    public static KeyBinding getAbilityKeyBind(Item item, Ability ability) {
        return itemAbilityBinds.getOrDefault(item, new HashMap<>()).getOrDefault(ability, null);
    }

    public static int getAbilityDefaultKey(Item item, Ability ability) {
        return itemAbilityDefaultKeys.getOrDefault(item, new HashMap<>()).getOrDefault(ability, GLFW.GLFW_KEY_UNKNOWN);
    }

    public static void init() {
        // registerDefaultBind(ModItems.NIGHT_VISION_GOGGLES, ToggledItem.TOGGLE_ABILITY, GLFW.GLFW_KEY_V);
        // registerDefaultBind(ModItems.THREAT_VISOR, ToggledItem.TOGGLE_ABILITY, GLFW.GLFW_KEY_V);
        //
        // registerDefaultBind(ModItems.TESLA_PACK, ToggledItem.TOGGLE_ABILITY, GLFW.GLFW_KEY_B);
        // registerDefaultBind(ModItems.HOVER_PACK, ToggledItem.TOGGLE_ABILITY, GLFW.GLFW_KEY_B);
        // registerDefaultBind(ModItems.CLOAKING_DEVICE, ToggledItem.TOGGLE_ABILITY, GLFW.GLFW_KEY_B);
        // registerDefaultBind(ModItems.SHIELD_GENERATOR, ToggledItem.TOGGLE_ABILITY, GLFW.GLFW_KEY_B);
        //
        // registerDefaultBind(ModItems.OBSIDIAN_SASH, ObsidianSashItem.ACTIVATE, GLFW.GLFW_KEY_G);

        ModItems.modItems.forEach(item -> {
            if (item instanceof IHasAbilities) {
                registerItemAbilities(item, "gadgets");
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            itemAbilityBinds.forEach((item, abilityBinds) -> {
                abilityBinds.forEach((ability, binding) -> {
                    while (binding.wasPressed()) {
                        C2SPacketSender.triggerAbility(item, ability);
                    }
                });
            });

            // while (ACTIVATE_OBSIDIAN_SASH_KEY.wasPressed()) {
            //     C2SPacketSender.triggerAbility(ModItems.OBSIDIAN_SASH);
            // }
            //
            // while (TOGGLE_NIGHT_VISION_GOGGLES.wasPressed()) {
            //     C2SPacketSender.triggerAbility(ModItems.NIGHT_VISION_GOGGLES);
            // }
        });
    }
}
