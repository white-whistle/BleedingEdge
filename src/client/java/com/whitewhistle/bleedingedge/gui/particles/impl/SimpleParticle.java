package com.whitewhistle.bleedingedge.gui.particles.impl;

import com.whitewhistle.bleedingedge.util.TweenUtil;
import com.whitewhistle.bleedingedge.util.VectorUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector2f;

public class SimpleParticle extends LifetimeParticle {
    public final Vector2f pos;
    public final Vector2f velocity = new Vector2f();
    public final Identifier texture;
    public final TexCoords texCoords;

    public float scale = 0;
    public float rotation = 0;

    private final Vector2f prevPos;
    private float prevScale = 0;
    private float prevRotation = 0;

    public SimpleParticle(int lifetime, Vector2f pos, Identifier texture, TexCoords texCoords) {
        super(lifetime);
        this.pos = pos;
        this.prevPos = new Vector2f(pos);
        this.texture = texture;
        this.texCoords = texCoords;
    }

    @Override
    public void tick() {
        super.tick();

        this.prevScale = scale;
        this.prevRotation = this.rotation;
        this.pos.add(velocity);
    }

    @Override
    public void render(Screen screen, DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
        var cScale = TweenUtil.lerp(this.prevScale, this.scale, tickDelta);
        var cRot = TweenUtil.lerp(this.prevRotation, this.rotation, tickDelta);
        var cPos = this.prevPos.lerp(this.pos, tickDelta);

        var matrix = drawContext.getMatrices();
        matrix.push();
        matrix.translate(cPos.x, cPos.y, 0);
        matrix.multiply(new Quaternionf().rotationZ((float) Math.toRadians(cRot)));
        matrix.translate(-((this.texCoords.w * cScale) / 2), -(((this.texCoords.h * cScale) / 2)), 0);
        matrix.scale(cScale, cScale, 1);

        drawContext.drawTexture(texture, 0, 0, this.texCoords.u, this.texCoords.v, this.texCoords.w, this.texCoords.h, this.texCoords.textureWidth, this.texCoords.textureHeight);
        matrix.pop();
    }

    public SimpleParticle randomizeVelocity(float size) {

        this.velocity.set(VectorUtil.randomDirection().mul(size));

        return this;
    }

    public SimpleParticle randomizeOffset(float size) {

        this.pos.add(VectorUtil.randomDirection().mul(size));

        return this;
    }

    public record TexCoords(int u, int v, int w, int h, int textureWidth, int textureHeight) {}
}