package com.flechazo.nekoration.client.rendering.blockentities;

import com.flechazo.nekoration.blocks.ItemDisplayBlock;
import com.flechazo.nekoration.blocks.entities.ItemDisplayBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CupboardRenderer implements BlockEntityRenderer<ItemDisplayBlockEntity> {
    private final Font font;

    public CupboardRenderer(BlockEntityRendererProvider.Context ctx) {
        this.font = ctx.getFont();
    }

    @Override
    public void render(ItemDisplayBlockEntity tileEntity, float partialTicks, PoseStack stack, MultiBufferSource buffers, int combinedLight, int combinedOverlay) {
        stack.pushPose();
        startTransform(tileEntity, stack);
        if (tileEntity.wallShelf)
            renderShelfItems(tileEntity, stack, buffers, combinedLight);
        else
            renderCabinetItems(tileEntity, stack, buffers, combinedLight);
        stack.popPose();
    }

    private void startTransform(ItemDisplayBlockEntity tileEntity, PoseStack stack) {
        stack.translate(0.5D, 0.5D, 0.5D);
        float angle = -tileEntity.getBlockState().getValue(ItemDisplayBlock.FACING).get2DDataValue() * 90.0F;
        stack.mulPose(Axis.YP.rotationDegrees(angle));
        stack.scale(0.5F, 0.5F, 0.5F);
        stack.mulPose(Axis.XP.rotationDegrees(-10));
    }

    private void renderItem(ItemStack item, PoseStack stack, MultiBufferSource buffers, int combinedLight, int randSeed) {
        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.GROUND, combinedLight, OverlayTexture.NO_OVERLAY, stack, buffers, null, randSeed);
    }

    private void renderShelfItems(ItemDisplayBlockEntity tileEntity, PoseStack stack, MultiBufferSource buffers, int combinedLight) {
        stack.translate(-1.35D, 0.2D, -0.5D);
        int rand = (int) tileEntity.getBlockPos().asLong();
        for (int i = 0; i < 4; i++) {
            stack.translate(0.55D, 0.0D, 0.0D);
            renderItem(tileEntity.renderItems[i], stack, buffers, combinedLight, rand + i + 1);
        }
    }

    private void renderCabinetItems(ItemDisplayBlockEntity tileEntity, PoseStack stack, MultiBufferSource buffers, int combinedLight) {
        int rand = (int) tileEntity.getBlockPos().asLong();

        stack.translate(-0.4D, 0.2D, -0.5D);
        renderItem(tileEntity.renderItems[0], stack, buffers, combinedLight, rand + 1);

        stack.translate(0.8D, 0.0D, 0.0D);
        renderItem(tileEntity.renderItems[1], stack, buffers, combinedLight, rand + 2);

        stack.translate(0.0D, -0.7D, -0.2D);
        renderItem(tileEntity.renderItems[3], stack, buffers, combinedLight, rand + 3);

        stack.translate(-0.8D, 0.0D, 0.0D);
        renderItem(tileEntity.renderItems[2], stack, buffers, combinedLight, rand + 4);
    }

    @Override
    public boolean shouldRenderOffScreen(ItemDisplayBlockEntity blockEntity) {
        return false;
    }
}
