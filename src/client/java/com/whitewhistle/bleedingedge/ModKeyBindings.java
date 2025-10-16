package com.whitewhistle.bleedingedge;

import com.whitewhistle.bleedingedge.items.ModItems;
import com.whitewhistle.bleedingedge.network.C2SPacketSender;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final KeyBinding ACTIVATE_OBSIDIAN_SASH_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.bleeding-edge.activate_obsidian_sash", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_G, // The keycode of the key
            "category.bleeding-edge.gadget_key_binding" // The translation key of the keybinding's category.
    ));

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ACTIVATE_OBSIDIAN_SASH_KEY.wasPressed()) {
                // var player = client.player;

                C2SPacketSender.triggerItem(ModItems.OBSIDIAN_SASH);

            }
        });
    }
}
