package com.whitewhistle.bleedingedge.shaders;

import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;

public class ModShaders {
    public static final ManagedShaderEffect GRAYSCALE = ShaderEffectManager.getInstance()
            .manage(ModIdentifier.of("shaders/post/grayscale.json"));


    public static void register() {
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            var client = MinecraftClient.getInstance();
            if (client == null) return;

            var player = client.player;
            if (player == null) return;

            if (player.hasStatusEffect(ModStatusEffects.IR_VISION)) {
                ModShaders.GRAYSCALE.render(tickDelta);
            }
        });
    }
}
