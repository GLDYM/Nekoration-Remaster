package com.flechazo.nekoration.client.rendering.blockentities;

import com.flechazo.nekoration.blocks.EaselMenuBlock;
import com.flechazo.nekoration.blocks.entities.EaselMenuBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;

public class EaselMenuRenderer implements BlockEntityRenderer<EaselMenuBlockEntity> {
    Font font;

    public EaselMenuRenderer(BlockEntityRendererProvider.Context ctx) {
        font = ctx.getFont();
    }

    @Override
    public void render(EaselMenuBlockEntity tileEntity, float partialTicks, PoseStack stack, MultiBufferSource buffers, int combinedLight, int combinedOverlay) {
        for (int rot = 0; rot < 2; rot++) {
            stack.pushPose();
            // Items on Front Side...
            stack.translate(0.5D, 0.5D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-tileEntity.getBlockState().getValue(EaselMenuBlock.FACING).get2DDataValue() * 90.0F + rot * 180.0F));

            float sc = 0.5F;
            stack.scale(sc, sc, sc);

            stack.mulPose(Axis.XP.rotationDegrees(-22.5F));

            int rand = (int) tileEntity.getBlockPos().asLong();

            // 0 1  // 4 5
            // 2 3  // 6 7
            stack.translate(-0.3D, 0.0D, 0.4D);
            Minecraft.getInstance().getItemRenderer().renderStatic(tileEntity.renderItems[rot * 4], ItemDisplayContext.GROUND,
                    combinedLight, OverlayTexture.NO_OVERLAY, stack, buffers, null, rand + 1);

            stack.translate(0.6D, 0.0D, 0.0D);
            Minecraft.getInstance().getItemRenderer().renderStatic(tileEntity.renderItems[1 + rot * 4], ItemDisplayContext.GROUND,
                    combinedLight, OverlayTexture.NO_OVERLAY, stack, buffers, null, rand + 2);

            stack.translate(0.0D, -0.6D, 0.0D);
            Minecraft.getInstance().getItemRenderer().renderStatic(tileEntity.renderItems[3 + rot * 4], ItemDisplayContext.GROUND,
                    combinedLight, OverlayTexture.NO_OVERLAY, stack, buffers, null, rand + 3);

            stack.translate(-0.6D, 0.0D, 0.0D);
            Minecraft.getInstance().getItemRenderer().renderStatic(tileEntity.renderItems[2 + rot * 4], ItemDisplayContext.GROUND,
                    combinedLight, OverlayTexture.NO_OVERLAY, stack, buffers, null, rand + 4);

            stack.popPose();
            // Texts on Front Side
            stack.pushPose();

            stack.translate(0.5D, 0.5D, 0.5D);
            stack.mulPose(Axis.YP.rotationDegrees(-tileEntity.getBlockState().getValue(EaselMenuBlock.FACING).get2DDataValue() * 90.0F + rot * 180.0F));
            stack.translate(-0.3D, 0.4D, 0.08D);
            stack.mulPose(Axis.XP.rotationDegrees(-22.5F));

            sc = 0.015F;
            stack.scale(sc, -sc, sc);

            DyeColor[] colors = tileEntity.getColors();

            if (tileEntity.getGlowing()) {
                for (int i = 0; i < 4; i++) {
                    font.drawInBatch(tileEntity.getMessage(i + rot * 4), 1.0F, 1.0F, colors[i + rot * 4].getTextColor(), false, stack.last().pose(), buffers, Font.DisplayMode.SEE_THROUGH, 0, combinedLight);
                    stack.translate(0.0F, 12.0F, 0.0F);
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    font.drawInBatch(tileEntity.getMessage(i + rot * 4), 1.0F, 1.0F, colors[i + rot * 4].getTextColor(), false, stack.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, combinedLight);
                    stack.translate(0.0F, 12.0F, 0.0F);
                }
            }

            stack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(EaselMenuBlockEntity BlockEntity) {
        return false;
    }
}