package com.whitewhistle.bleedingedge.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ThreatOverlayRenderer {

    private static final float MAX_DISTANCE = 40;
    private static final Identifier RAID_ICON_ATLAS = ModIdentifier.of("textures/gui/threat_atlas.png");

    private static final SpriteUV RAID_DEFEND = new SpriteUV(0, 0, 1f, 1f);

    private static final float FULL_SIZE = 16;

    private static final List<LivingEntity> renderedEntities = new ArrayList<>();
    private static boolean enabled = false;

    public static void prepareRenderInWorld(LivingEntity entity) {
        if (!enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (entity.isDead()) return;
        if (entity.getAttributeValue(ModEntityAttributes.THREAT) <= 0) return;

        if (entity.distanceTo(client.getCameraEntity()) > MAX_DISTANCE) {
            return;
        }

        renderedEntities.add(entity);

    }

    public static void renderInWorld(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider , Camera camera) {
        if (!enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (camera == null) {
            camera = client.getEntityRenderDispatcher().camera;
        }

        if (camera == null) {
            renderedEntities.clear();
            return;
        }

        if (renderedEntities.isEmpty()) {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
                GL11.GL_ZERO);

        for (LivingEntity entity : renderedEntities) {

            float scaleToGui = 0.025f;
            boolean sneaking = entity.isInSneakingPose();
            float height = entity.getHeight() + 0.6F - (sneaking ? 0.25F : 0.0F);

            float tickDelta = client.getTickDelta();
            double x = MathHelper.lerp((double) tickDelta, entity.prevX, entity.getX());
            double y = MathHelper.lerp((double) tickDelta, entity.prevY, entity.getY());
            double z = MathHelper.lerp((double) tickDelta, entity.prevZ, entity.getZ());

            Vec3d camPos = camera.getPos();
            double camX = camPos.x;
            double camY = camPos.y;
            double camZ = camPos.z;


            matrix.push();
            matrix.translate(x - camX, (y + height) - camY, z - camZ);
            matrix.multiply(new Quaternionf().rotationY((float) Math.toRadians(-camera.getYaw())));
            matrix.multiply(new Quaternionf().rotationX((float) Math.toRadians(camera.getPitch())));
            matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

            renderEntityOverlays(matrix, vertexConsumerProvider ,entity, 0, 0);

            matrix.pop();
        }

        RenderSystem.disableBlend();

        renderedEntities.clear();
    }

    public static void renderEntityOverlays(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, LivingEntity entity, double x, double y) {
        Matrix4f m4f = matrix.peek().getPositionMatrix();
        MinecraftClient client = MinecraftClient.getInstance();
        var textRenderer = client.textRenderer;

        var visibleThroughObjects = false;


        var text = "" + (int)entity.getAttributeValue(ModEntityAttributes.THREAT);
        var textWidth = textRenderer.getWidth(text);

        drawSprite(m4f, x, y - 16, FULL_SIZE, RAID_DEFEND);

        matrix.push();
        matrix.translate(0,0,0.1);
        var m4fShadow = matrix.peek().getPositionMatrix();

        for (int i = 0 ; i < 3; i ++) {
            for (int j = 0 ; j < 3 ; j ++) {
                if (i == 1 && j == 1) continue;

                textRenderer.draw(text, (float) x - (textWidth/2f) + i - 1, (float) y + 4 + j - 1, 0xd95763, false, m4fShadow, vertexConsumerProvider, visibleThroughObjects ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL,
                        0,
                        15728880);
            }
        }

        textRenderer.draw(text, (float) x - (textWidth/2f), (float) y + 4, 0, false, m4f, vertexConsumerProvider, visibleThroughObjects ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL,
                0,
                15728880);

        matrix.pop();


    }


    private static void drawSprite(Matrix4f matrix4f, double x, double y, float size, SpriteUV spriteUV) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, RAID_ICON_ATLAS);
        RenderSystem.enableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float offsetX = -size / 2;
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        var u = spriteUV.minU();
        var v = spriteUV.minV();
        var mu = spriteUV.maxU();
        var mv = spriteUV.maxV();

        buffer.vertex(matrix4f, (float) (x + offsetX), (float) y, 0)
                .texture(u, v).next();
        buffer.vertex(matrix4f, (float) (x + offsetX), (float) (y + size), 0)
                .texture(u, mv).next();
        buffer.vertex(matrix4f, (float) (x + offsetX + size), (float) (y + size), 0)
                .texture(mu, mv).next();
        buffer.vertex(matrix4f, (float) (x + offsetX + size), (float) y, 0)
                .texture(mu, v).next();

        tessellator.draw();
    }

    private static boolean computeEnabled() {
        var client = MinecraftClient.getInstance();
        var player = client.player;
        if (player == null) return false;

        return player.hasStatusEffect(ModStatusEffects.THREAT_ANALYSIS);
    }

    public static void register() {
        WorldRenderEvents.BEFORE_ENTITIES.register(ctx -> {
            enabled = computeEnabled();
        });

        WorldRenderEvents.AFTER_ENTITIES.register(ctx -> {
            renderInWorld(ctx.matrixStack(), ctx.consumers(), ctx.camera());
        });
    }

    public record SpriteUV(float minU, float minV, float maxU, float maxV) {

    }
}