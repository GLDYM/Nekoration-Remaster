package com.flechazo.nekoration.client.rendering.blockentities;

import com.flechazo.nekoration.blocks.entities.PrismapTableBlockEntity;
import com.flechazo.nekoration.client.rendering.ChunkModel;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher.RenderChunk;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class PrismapTableRenderer implements BlockEntityRenderer<PrismapTableBlockEntity> {
    private static Minecraft mc;
    private static boolean error = false;

    public PrismapTableRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(PrismapTableBlockEntity tile, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light, int overlay) {
        if (error) return;

        try {
            init();

            ChunkModel model = tile.chunkModel;
            if (model == null) return;

            if (!model.isCompiled() && !model.isCompiling()) {
                model.compile(mc, tile.getBlockPos());
            }

            renderMiniWorld(tile, model, stack);
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        }
    }

    private void init() {
        if (mc == null) mc = Minecraft.getInstance();
    }

    private void renderMiniWorld(PrismapTableBlockEntity tile, ChunkModel model, PoseStack stack) {
        BlockPos origin = tile.getBlockPos();
        Matrix4f projection = RenderSystem.getProjectionMatrix();

        stack.pushPose();
        stack.translate(0.5, 0.75, 0.5);
        stack.scale(0.0125F, 0.0125F, 0.0125F);

        double chunkSize = 16.0;
        int r = tile.viewAreaRadius;
        int maxY = tile.getLevel().getSectionsCount();

        for (int cy = 0; cy < maxY; cy++) {
            for (int cx = -r; cx <= r; cx++) {
                for (int cz = -r; cz <= r; cz++) {
                    RenderChunk chunk = model.getRenderChunk(cx + r, cy, cz + r);
                    double offsetX = chunkSize * cx;
                    double offsetZ = chunkSize * cz;

                    stack.translate(offsetX, 0, offsetZ);
                    renderChunkLayers(chunk, stack, origin, projection);
                    stack.translate(-offsetX, 0, -offsetZ);
                }
            }
            stack.translate(0, chunkSize, 0);
        }

        stack.popPose();
    }

    private void renderChunkLayers(RenderChunk chunk, PoseStack stack, BlockPos origin, Matrix4f projection) {
        renderLayer(RenderType.solid(), chunk, stack, origin, projection);
        toggleAtlasBlur(true);
        renderLayer(RenderType.cutoutMipped(), chunk, stack, origin, projection);
        toggleAtlasBlur(false);
        renderLayer(RenderType.cutout(), chunk, stack, origin, projection);
    }

    private void toggleAtlasBlur(boolean enable) {
        TextureAtlas atlas = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        if (enable) {
            atlas.setBlurMipmap(false, mc.options.mipmapLevels().get() > 0);
        } else {
            atlas.restoreLastBlurMipmap();
        }
    }

    private void renderLayer(RenderType type, RenderChunk chunk, PoseStack stack, BlockPos origin, Matrix4f projection) {
        if (chunk.getCompiledChunk().isEmpty(type)) return;

        type.setupRenderState();

        ShaderInstance shader = RenderSystem.getShader();
        BufferUploader.reset();

        applyShaderUniforms(shader, stack.last().pose(), projection);
        shader.apply();

        VertexBuffer buffer = chunk.getBuffer(type);
        GlStateManager._blendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GlStateManager._enableBlend();

        buffer.bind();
        buffer.draw();

        shader.clear();
        VertexBuffer.unbind();
        type.clearRenderState();
    }

    private void applyShaderUniforms(ShaderInstance shader, Matrix4f modelView, Matrix4f projection) {
        for (int k = 0; k < 12; ++k) {
            int textureId = RenderSystem.getShaderTexture(k);
            shader.setSampler("Sampler" + k, textureId);
        }

        if (shader.MODEL_VIEW_MATRIX != null) shader.MODEL_VIEW_MATRIX.set(modelView);
        if (shader.PROJECTION_MATRIX != null) shader.PROJECTION_MATRIX.set(projection);
        if (shader.COLOR_MODULATOR != null) shader.COLOR_MODULATOR.set(RenderSystem.getShaderColor());
        if (shader.TEXTURE_MATRIX != null) shader.TEXTURE_MATRIX.set(RenderSystem.getTextureMatrix());
        if (shader.GAME_TIME != null) shader.GAME_TIME.set(RenderSystem.getShaderGameTime());

        RenderSystem.setupShaderLights(shader);
    }

    @Override
    public boolean shouldRenderOffScreen(PrismapTableBlockEntity blockEntity) {
        return true;
    }
}
