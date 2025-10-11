package com.whitewhistle.bleedingedge.gui.particles.impl;

import com.whitewhistle.bleedingedge.gui.particles.GuiParticle;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

public class ArrowParticle extends SimpleParticle {
    public static final Identifier TEXTURE = ModIdentifier.of("textures/gui/particle_atlas.png");
    public static final TexCoords GREEN = new TexCoords(0,0, 7, 8, 32, 32);
    public static final TexCoords RED = new TexCoords(6,0, 7, 8, 32,32);

    private final int direction;

    public ArrowParticle(Vector2f pos, TexCoords texCoords, int direction) {
        super(
                GuiParticle.random.nextInt(10, 20),
                pos,
                TEXTURE,
                texCoords
        );
        this.direction = direction;
    }

    @Override
    public void tick() {
        super.tick();

        float progress = getProgress();
        float sizeScalar = (float) Math.cos(progress * Math.PI / 2);

        this.scale = sizeScalar;

        this.velocity.y += 4 * (1 - sizeScalar) * direction;
        this.velocity.mul(sizeScalar * sizeScalar);

        // this.rotation += this.velocity.x * 50;
    }
}
