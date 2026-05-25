package com.flechazo.nekoration.client.rendering.entities;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.client.rendering.RenderTypeHelper;
import com.flechazo.nekoration.entities.PaintingData;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.Closeable;

public abstract class AbstractPaintingRenderer implements Closeable {
    public abstract void render(PoseStack stack, Matrix4f pose, Matrix3f normal, MultiBufferSource buffers, PaintingData data, short blocHor, short blocVer, float left, float bottom, int light);

    public abstract void renderFull(PoseStack stack, Matrix4f pose, Matrix3f normal, MultiBufferSource buffers, PaintingData data, float left, float bottom, int light);

    @Override
    public void close() {
    }

    public static class PixelsPaintingRenderer extends AbstractPaintingRenderer {
        @Override
        public void render(PoseStack stack, Matrix4f pose, Matrix3f normal, MultiBufferSource buffers, PaintingData data, short blocHor, short blocVer, float left, float bottom, int light) {
            renderBlock(pose, normal, buffers, data, left, bottom, light, blocHor, blocVer, 16, 16);
        }

        @Override
        public void renderFull(PoseStack stack, Matrix4f pose, Matrix3f normal, MultiBufferSource buffers, PaintingData data, float left, float bottom, int light) {
            renderBlock(pose, normal, buffers, data, left, bottom, light, (short) 0, (short) 0,
                    data.getWidth(), data.getHeight());
        }

        private void renderBlock(Matrix4f pose, Matrix3f normal, MultiBufferSource buffers, PaintingData data, float left, float bottom, int light, short blocHor, short blocVer, int width, int height) {
            VertexConsumer vb = buffers.getBuffer(RenderTypeHelper.paintingPixels());
            light = Math.max(0, light - 0x300000);

            int baseX = blocHor * 16;
            int baseY = blocVer * 16;

            for (int x = 0; x < width; x++) {
                int px = data.getWidth() - 1 - (baseX + x);
                for (int y = 0; y < height; y++) {
                    int py = data.getHeight() - 1 - (baseY + y);
                    int[] rgb = NekoColors.getRGBArray(data.getCompositeAt(px, py));
                    addQuad(vb, pose, normal, left + x, bottom + y, rgb, light);
                }
            }
        }

        private void addQuad(VertexConsumer vb, Matrix4f pose, Matrix3f normal, float x, float y, int[] rgb, int light) {
            float z = -0.5F;
            int r = rgb[0], g = rgb[1], b = rgb[2];
            vertexPixel(pose, normal, vb, x + 1, y, z, 0, 0, -1, light, r, g, b);
            vertexPixel(pose, normal, vb, x, y, z, 0, 0, -1, light, r, g, b);
            vertexPixel(pose, normal, vb, x, y + 1, z, 0, 0, -1, light, r, g, b);
            vertexPixel(pose, normal, vb, x + 1, y + 1, z, 0, 0, -1, light, r, g, b);
        }
    }


    public static class ImagePaintingRenderer extends AbstractPaintingRenderer {
        private final DynamicTexture texture;
        private final RenderType renderType;

        public ImagePaintingRenderer() {
            this.texture = null;
            this.renderType = null;
        }

        public ImagePaintingRenderer(NativeImage nativeImage, TextureManager manager) {
            this.texture = new DynamicTexture(nativeImage);
            this.renderType = RenderTypeHelper.paintingTexture(manager.register("painting", texture));
        }

        @Override
        public void render(PoseStack stack, Matrix4f pose, Matrix3f normal, MultiBufferSource buffers,
                           PaintingData data, short blocHor, short blocVer, float left, float bottom, int light) {
            int horCount = data.getWidth() / 16;
            int verCount = data.getHeight() / 16;
            float u0 = (float) (1.0 * (horCount - blocHor) / horCount);
            float u1 = (float) (1.0 * (horCount - blocHor - 1) / horCount);
            float v0 = (float) (1.0 * (verCount - blocVer) / verCount);
            float v1 = (float) (1.0 * (verCount - blocVer - 1) / verCount);
            renderQuad(buffers, pose, normal, left, bottom, 16, 16, u0, v0, u1, v1, light);
        }

        @Override
        public void renderFull(PoseStack stack, Matrix4f pose, Matrix3f normal, MultiBufferSource buffers,
                               PaintingData data, float left, float bottom, int light) {
            renderQuad(buffers, pose, normal, left, bottom, data.getWidth(), data.getHeight(),
                    1.0F, 1.0F, 0.0F, 0.0F, light);
        }

        private void renderQuad(MultiBufferSource buffers, Matrix4f pose, Matrix3f normal,
                                float left, float bottom, float width, float height,
                                float u0, float v0, float u1, float v1, int light) {
            VertexConsumer vb = buffers.getBuffer(renderType);
            float right = left + width;
            float top = bottom + height;
            float z = -0.5F;

            vertexImage(pose, normal, vb, right, bottom, u1, v0, z, 0, 0, -1, light);
            vertexImage(pose, normal, vb, left, bottom, u0, v0, z, 0, 0, -1, light);
            vertexImage(pose, normal, vb, left, top, u0, v1, z, 0, 0, -1, light);
            vertexImage(pose, normal, vb, right, top, u1, v1, z, 0, 0, -1, light);
        }

        @Override
        public void close() {
            if (texture != null) texture.close();
        }
    }

    private static void vertexPixel(Matrix4f pose, Matrix3f normal, VertexConsumer vb, float x, float y, float z, int nx, int ny, int nz, int light, int r, int g, int b) {
        vb.vertex(pose, x, y, z).color(r, g, b, 255).uv2(light).normal(normal, nx, ny, nz).endVertex();
    }

    private static void vertexImage(Matrix4f pose, Matrix3f normal, VertexConsumer vb, float x, float y, float u, float v, float z, int nx, int ny, int nz, int light) {
        vb.vertex(pose, x, y, z).color(255, 255, 255, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
    }
}