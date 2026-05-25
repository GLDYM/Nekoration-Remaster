package com.flechazo.nekoration.client.rendering.entities;

import com.flechazo.nekoration.NekoConfig;
import com.flechazo.nekoration.client.rendering.PaintingRendererManager;
import com.flechazo.nekoration.entities.PaintingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PaintingRenderer extends EntityRenderer<PaintingEntity> {
    private static final Logger LOGGER = LogManager.getLogger("Painting Renderer");
    private static Font font;

    public PaintingRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        font = ctx.getFont();
    }

    @Override
    public void render(PaintingEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight) {
        if (entity.data == null) return;

        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - rotation));
        stack.scale(0.0625F, 0.0625F, 0.0625F); // Scale to block units (1/16)

        PaintingTextureManager textureManager = Minecraft.getInstance().getPaintingTextures();
        renderPainting(stack, buffers, entity, entity.getWidth(), entity.getHeight(), textureManager.getBackSprite());

        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PaintingEntity entity) {
        return Minecraft.getInstance().getPaintingTextures().getBackSprite().atlasLocation();
    }

    /**
     * Renders a painting entity with its frame and artwork
     */
    private void renderPainting(PoseStack stack, MultiBufferSource buffers, PaintingEntity entity, int width, int height, TextureAtlasSprite woodTex) {
        PoseStack.Pose poseEntry = stack.last();
        Matrix4f pose = poseEntry.pose();
        Matrix3f normal = poseEntry.normal();

        // Calculate painting boundaries
        float leftEdge = (float) (-width) / 2.0F;
        float topEdge = (float) (-height) / 2.0F;

        // Get wood texture coordinates
        float woodU0 = woodTex.getU0();
        float woodU1 = woodTex.getU1();
        float woodV0 = woodTex.getV0();
        float woodV1 = woodTex.getV1();
        float woodV_ = woodTex.getV(1.0D);
        float woodU_ = woodTex.getU(1.0D);

        // Calculate block counts
        short blocksHorizontal = (short) (width / 16);
        short blocksVertical = (short) (height / 16);

        // Get rendering configuration
        boolean fastRender = NekoConfig.CLIENT.simplifyRendering.get();
        boolean useImageRendering = NekoConfig.CLIENT.useImageRendering.get();
        boolean renderWithImage = entity.data.imageReady && useImageRendering;

        // Get entity light level
        int entityLight = LevelRenderer.getLightColor(entity.level(), entity.getPos());

        // Render painting block by block in fancy mode
        if (!fastRender) {
            renderDetailedPainting(stack, pose, normal, buffers, entity, leftEdge, topEdge,
                    blocksHorizontal, blocksVertical, woodTex, woodU0, woodU1, woodV0, woodV1, woodU_, woodV_,
                    renderWithImage, entityLight);
        } else {
            // Render frame for all blocks
            for (short blocHor = 0; blocHor < blocksHorizontal; ++blocHor) {
                for (short blocVer = 0; blocVer < blocksVertical; ++blocVer) {
                    renderFrameForBlock(pose, normal, buffers, entity, leftEdge, topEdge,
                            blocHor, blocVer, blocksHorizontal, blocksVertical,
                            woodU0, woodU1, woodV0, woodV1, woodU_, woodV_, entityLight);
                }
            }

            // Render the artwork in one go for fast mode
            renderArtwork(stack, pose, normal, buffers, entity, leftEdge, topEdge, renderWithImage, entityLight);
        }

        // Render debug information if enabled
        renderDebugInfo(stack, buffers, entity, leftEdge, topEdge, renderWithImage, fastRender, entityLight);
    }

    /**
     * Renders the painting in detailed mode (block by block)
     */
    private void renderDetailedPainting(PoseStack stack, Matrix4f pose, Matrix3f normal, MultiBufferSource buffers,
                                        PaintingEntity entity, float leftEdge, float topEdge,
                                        short blocksHorizontal, short blocksVertical,
                                        TextureAtlasSprite woodTex, float woodU0, float woodU1, float woodV0, float woodV1,
                                        float woodU_, float woodV_, boolean renderWithImage, int defaultLight) {

        for (short blocHor = 0; blocHor < blocksHorizontal; ++blocHor) { // BlockCount Horizontally...
            for (short blocVer = 0; blocVer < blocksVertical; ++blocVer) { // BlockCount Vertically...
                // Calculate block coordinates
                float right = leftEdge + (float) ((blocHor + 1) * 16);
                float left = leftEdge + (float) (blocHor * 16);
                float top = topEdge + (float) ((blocVer + 1) * 16);
                float bottom = topEdge + (float) (blocVer * 16);

                // Calculate accurate lighting for this block
                int light = calculateBlockLight(entity, left, right, top, bottom);

                // Render the frame for this block
                renderFrameForBlock(pose, normal, buffers, entity, leftEdge, topEdge,
                        blocHor, blocVer, blocksHorizontal, blocksVertical,
                        woodU0, woodU1, woodV0, woodV1, woodU_, woodV_, light);

                // Render the artwork for this block
                AbstractPaintingRenderer renderer = getPaintingRenderer(entity, renderWithImage);
                renderer.render(stack, pose, normal, buffers, entity.data, blocHor, blocVer, left, bottom, light);
            }
        }
    }

    /**
     * Renders the frame for a single block of the painting
     */
    private void renderFrameForBlock(Matrix4f pose, Matrix3f normal, MultiBufferSource buffers,
                                     PaintingEntity entity, float leftEdge, float topEdge,
                                     short blocHor, short blocVer, short blocksHorizontal, short blocksVertical,
                                     float woodU0, float woodU1, float woodV0, float woodV1,
                                     float woodU_, float woodV_, int light) {

        // Calculate block coordinates
        float right = leftEdge + (float) ((blocHor + 1) * 16);
        float left = leftEdge + (float) (blocHor * 16);
        float top = topEdge + (float) ((blocVer + 1) * 16);
        float bottom = topEdge + (float) (blocVer * 16);

        VertexConsumer vertexBuilder = buffers.getBuffer(RenderType.entitySolid(this.getTextureLocation(entity)));

        // Render back face (always rendered)
        renderFace(pose, normal, vertexBuilder, right, left, top, bottom, woodU0, woodU1, woodV0, woodV1, 0.5F, 0, 0, 1, light);

        // Render top edge if this is the top row
        if (blocVer == blocksVertical - 1) {
            renderTopFace(pose, normal, vertexBuilder, right, left, top, woodU0, woodU1, woodV0, woodV_, light);
        }

        // Render bottom edge if this is the bottom row
        if (blocVer == 0) {
            renderBottomFace(pose, normal, vertexBuilder, right, left, bottom, woodU0, woodU1, woodV0, woodV_, light);
        }

        // Render right edge if this is the rightmost column
        if (blocHor == blocksHorizontal - 1) {
            renderRightFace(pose, normal, vertexBuilder, right, top, bottom, woodU_, woodU0, woodV0, woodV1, light);
        }

        // Render left edge if this is the leftmost column
        if (blocHor == 0) {
            renderLeftFace(pose, normal, vertexBuilder, left, top, bottom, woodU_, woodU0, woodV0, woodV1, light);
        }
    }

    /**
     * Renders the back face of the painting frame
     */
    private void renderFace(Matrix4f pose, Matrix3f normal, VertexConsumer vertexBuilder,
                            float right, float left, float top, float bottom,
                            float u0, float u1, float v0, float v1,
                            float z, int nx, int ny, int nz, int light) {
        // Pos[Z] // B[ack]
        vertexFrame(pose, normal, vertexBuilder, right, top, u0, v0, z, nx, ny, nz, light);
        vertexFrame(pose, normal, vertexBuilder, left, top, u1, v0, z, nx, ny, nz, light);
        vertexFrame(pose, normal, vertexBuilder, left, bottom, u1, v1, z, nx, ny, nz, light);
        vertexFrame(pose, normal, vertexBuilder, right, bottom, u0, v1, z, nx, ny, nz, light);
    }

    /**
     * Renders the top face of the painting frame
     */
    private void renderTopFace(Matrix4f pose, Matrix3f normal, VertexConsumer vertexBuilder,
                               float right, float left, float top,
                               float u0, float u1, float v0, float v_,
                               int light) {
        // Pos[Y] // U[p]
        vertexFrame(pose, normal, vertexBuilder, right, top, u0, v0, -0.5F, 0, 1, 0, light);
        vertexFrame(pose, normal, vertexBuilder, left, top, u1, v0, -0.5F, 0, 1, 0, light);
        vertexFrame(pose, normal, vertexBuilder, left, top, u1, v_, 0.5F, 0, 1, 0, light);
        vertexFrame(pose, normal, vertexBuilder, right, top, u0, v_, 0.5F, 0, 1, 0, light);
    }

    /**
     * Renders the bottom face of the painting frame
     */
    private void renderBottomFace(Matrix4f pose, Matrix3f normal, VertexConsumer vertexBuilder,
                                  float right, float left, float bottom,
                                  float u0, float u1, float v0, float v_,
                                  int light) {
        // Neg[Y] // D[own]
        vertexFrame(pose, normal, vertexBuilder, right, bottom, u0, v0, 0.5F, 0, -1, 0, light);
        vertexFrame(pose, normal, vertexBuilder, left, bottom, u1, v0, 0.5F, 0, -1, 0, light);
        vertexFrame(pose, normal, vertexBuilder, left, bottom, u1, v_, -0.5F, 0, -1, 0, light);
        vertexFrame(pose, normal, vertexBuilder, right, bottom, u0, v_, -0.5F, 0, -1, 0, light);
    }

    /**
     * Renders the right face of the painting frame
     */
    private void renderRightFace(Matrix4f pose, Matrix3f normal, VertexConsumer vertexBuilder,
                                 float right, float top, float bottom,
                                 float u_, float u0, float v0, float v1,
                                 int light) {
        // Neg[X] // L[eft]
        vertexFrame(pose, normal, vertexBuilder, right, top, u_, v0, 0.5F, -1, 0, 0, light);
        vertexFrame(pose, normal, vertexBuilder, right, bottom, u_, v1, 0.5F, -1, 0, 0, light);
        vertexFrame(pose, normal, vertexBuilder, right, bottom, u0, v1, -0.5F, -1, 0, 0, light);
        vertexFrame(pose, normal, vertexBuilder, right, top, u0, v0, -0.5F, -1, 0, 0, light);
    }

    /**
     * Renders the left face of the painting frame
     */
    private void renderLeftFace(Matrix4f pose, Matrix3f normal, VertexConsumer vertexBuilder,
                                float left, float top, float bottom,
                                float u_, float u0, float v0, float v1,
                                int light) {
        // Pos[X] // R[ight]
        vertexFrame(pose, normal, vertexBuilder, left, top, u_, v0, -0.5F, 1, 0, 0, light);
        vertexFrame(pose, normal, vertexBuilder, left, bottom, u_, v1, -0.5F, 1, 0, 0, light);
        vertexFrame(pose, normal, vertexBuilder, left, bottom, u0, v1, 0.5F, 1, 0, 0, light);
        vertexFrame(pose, normal, vertexBuilder, left, top, u0, v0, 0.5F, 1, 0, 0, light);
    }

    /**
     * Calculates the light level for a specific block in the painting
     */
    private int calculateBlockLight(PaintingEntity entity, float left, float right, float top, float bottom) {
        // Get the accurate Block Position, thus get a better lighting value
        int blockX = Mth.floor(entity.getX());
        int blockY = Mth.floor(entity.getY() + (double) ((top + bottom) / 2.0F / 16.0F));
        int blockZ = Mth.floor(entity.getZ());

        Direction direction = entity.getDirection();
        if (direction == Direction.NORTH) {
            blockX = Mth.floor(entity.getX() + (double) ((right + left) / 2.0F / 16.0F));
        } else if (direction == Direction.WEST) {
            blockZ = Mth.floor(entity.getZ() - (double) ((right + left) / 2.0F / 16.0F));
        } else if (direction == Direction.SOUTH) {
            blockX = Mth.floor(entity.getX() - (double) ((right + left) / 2.0F / 16.0F));
        } else if (direction == Direction.EAST) {
            blockZ = Mth.floor(entity.getZ() + (double) ((right + left) / 2.0F / 16.0F));
        }

        return LevelRenderer.getLightColor(entity.level(), new BlockPos(blockX, blockY, blockZ));
    }

    /**
     * Gets the appropriate painting renderer based on configuration
     */
    private AbstractPaintingRenderer getPaintingRenderer(PaintingEntity entity, boolean renderWithImage) {
        AbstractPaintingRenderer renderer;

        if (renderWithImage) {
            renderer = PaintingRendererManager.get(entity.data.getPaintingHash());
            if (renderer == null) {
                // Reset to the Pixel Renderer if image renderer is not ready
                LOGGER.error("Image Renderer Not Ready!");
                entity.data.imageReady = false;
                renderer = PaintingRendererManager.PixelsRenderer();
            }
        } else {
            renderer = PaintingRendererManager.PixelsRenderer();
        }

        return renderer;
    }

    /**
     * Renders the artwork for fast rendering mode
     */
    private void renderArtwork(PoseStack stack, Matrix4f pose, Matrix3f normal, MultiBufferSource buffers,
                               PaintingEntity entity, float leftEdge, float topEdge,
                               boolean renderWithImage, int light) {
        // Get the appropriate renderer
        AbstractPaintingRenderer renderer = getPaintingRenderer(entity, renderWithImage);

        // Render the artwork in one go
        renderer.renderFull(stack, pose, normal, buffers, entity.data, leftEdge, topEdge, light);
    }

    /**
     * Renders debug information if debug mode is enabled
     */
    private void renderDebugInfo(PoseStack stack, MultiBufferSource buffers, PaintingEntity entity,
                                 float leftEdge, float topEdge, boolean renderWithImage,
                                 boolean fastRender, int entityLight) {
        if (!NekoConfig.CLIENT.debugMode.get()) {
            return;
        }

        // Draw Debug Text...
        stack.pushPose();
        stack.translate(-leftEdge - 1.0D, topEdge + 3.0D, -0.6D);
        stack.scale(-0.2F, -0.2F, 0.2F);

        // Render mode information
        String renderMode = (renderWithImage ? "Rendered with Image" : "Rendered Pixel-by-Pixel") +
                (fastRender ? " (Fast Mode)" : " (Fancy Mode)");
        font.drawInBatch(renderMode, 1.0F, 1.0F, 0xFFFFFF, false,
                stack.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, entityLight);

        // Render hash and light level
        stack.translate(0.0D, -10.0D, 0.0D);
        font.drawInBatch("#" + entity.data.getPaintingHash() + String.format(" L: %x", entityLight),
                1.0F, 1.0F, 0xFFFFFF, false,
                stack.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, entityLight);

        // Render data UUID
        stack.translate(0.0D, 30.0D, 0.0D);
        font.drawInBatch(String.valueOf(entity.data.getUUID()),
                1.0F, 1.0F, 0xFFFFFF, false,
                stack.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, entityLight);

        // Render entity UUID
        stack.translate(0.0D, 10.0D, 0.0D);
        font.drawInBatch(String.valueOf(entity.getUUID()),
                1.0F, 1.0F, 0xFFFFFF, false,
                stack.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, entityLight);

        stack.popPose();
    }

    /**
     * Helper method to create a vertex for the painting frame
     */
    private static void vertexFrame(Matrix4f pose, Matrix3f normal, VertexConsumer vertexBuilder,
                                    float x, float y, float u, float v, float z,
                                    int nx, int ny, int nz, int light) {
        vertexBuilder.vertex(pose, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, (float) nx, (float) ny, (float) nz)
                .endVertex();
    }
}