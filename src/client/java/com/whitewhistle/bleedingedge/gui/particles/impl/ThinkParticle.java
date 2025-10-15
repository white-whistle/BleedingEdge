package com.whitewhistle.bleedingedge.gui.particles.impl;

import com.whitewhistle.bleedingedge.gui.particles.GuiParticle;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import com.whitewhistle.bleedingedge.util.RandomUtil;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

import java.util.List;

public class ThinkParticle extends SimpleParticle {
    public static final Identifier TEXTURE = ModIdentifier.of("textures/gui/particle_atlas.png");
    public static final List<TexCoords> SPRITES = List.of(
            new TexCoords(13,0, 7 + 1, 8 + 1, 32, 32),
            new TexCoords(20,0, 7 + 1, 6 + 1, 32, 32),
            new TexCoords(27,0, 5 + 1, 5 + 1, 32, 32),
            new TexCoords(13,8, 7 + 1, 9 + 1, 32, 32),
            new TexCoords(20,8, 7 + 1, 7 + 1, 32, 32),
            new TexCoords(13,17, 10 + 1, 7 + 1, 32, 32)
    );


    public ThinkParticle(Vector2f pos) {
        super(
                GuiParticle.random.nextInt(10, 20),
                pos,
                TEXTURE,
                RandomUtil.sample(SPRITES)
        );
    }

    @Override
    public void tick() {
        super.tick();

        float progress = getProgress();
        float sizeScalar = (float) Math.cos(progress * Math.PI / 2);

        this.scale = sizeScalar * 0.5f;

        // this.velocity.y += 4 * (1 - sizeScalar) * direction;
        this.velocity.mul(sizeScalar * sizeScalar);

        // this.rotation += this.velocity.x * 50;
    }
}
