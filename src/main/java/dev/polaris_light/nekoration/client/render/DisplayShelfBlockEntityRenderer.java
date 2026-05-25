package dev.polaris_light.nekoration.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.polaris_light.nekoration.block.entity.DisplayShelfBlockEntity;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class DisplayShelfBlockEntityRenderer implements BlockEntityRenderer<DisplayShelfBlockEntity> {
    public DisplayShelfBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(
        DisplayShelfBlockEntity blockEntity,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay
    ) {
        List<ItemStack> displayItems = blockEntity.getDisplayItems();
        if (displayItems.stream().allMatch(ItemStack::isEmpty)) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        float angle = -blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING).get2DDataValue() * 90.0F;
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        // poseStack.mulPose(Axis.XP.rotationDegrees(-10.0F));

        if (blockEntity.isWallMounted()) {
            renderWallShelf(displayItems, blockEntity, poseStack, buffer, packedLight);
        } else {
            renderShelf(displayItems, blockEntity, poseStack, buffer, packedLight);
        }
        poseStack.popPose();
    }

    private void renderWallShelf(
        List<ItemStack> displayItems,
        DisplayShelfBlockEntity blockEntity,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight
    ) {
        poseStack.translate(-1.2D, 0.2D, -0.5D);
        int seed = (int) blockEntity.getBlockPos().asLong();
        for (int i = 0; i < displayItems.size(); i++) {
            poseStack.translate(0.45D, 0.0D, 0.0D);
            renderItem(displayItems.get(i), poseStack, buffer, packedLight, seed + i + 1);
        }
    }

    private void renderShelf(
        List<ItemStack> displayItems,
        DisplayShelfBlockEntity blockEntity,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight
    ) {
        int seed = (int) blockEntity.getBlockPos().asLong();

        poseStack.translate(-0.4D, 0.2D, -0.5D);
        renderItem(displayItems.get(0), poseStack, buffer, packedLight, seed + 1);

        poseStack.translate(0.8D, 0.0D, 0.0D);
        renderItem(displayItems.get(1), poseStack, buffer, packedLight, seed + 2);

        poseStack.translate(0.0D, -0.7D, 0.0D);
        renderItem(displayItems.get(3), poseStack, buffer, packedLight, seed + 3);

        poseStack.translate(-0.8D, 0.0D, 0.0D);
        renderItem(displayItems.get(2), poseStack, buffer, packedLight, seed + 4);
    }

    private void renderItem(ItemStack itemStack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int seed) {
        if (itemStack.getItem() instanceof BlockItem) {
            poseStack.translate(0.0D, -0.27D, 0.0D);
            poseStack.scale(1.25F, 1.25F, 1.25F);
            Minecraft.getInstance()
                .getItemRenderer()
                .renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, seed);
            poseStack.scale(0.8F, 0.8F, 0.8F);
            poseStack.translate(0.0D, 0.27D, 0.0D);
        } else {
            poseStack.translate(0.0D, -0.125D, 0.0D);
            Minecraft.getInstance()
                .getItemRenderer()
                .renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, seed);
            poseStack.translate(0.0D, 0.125D, 0.0D);
        }
    }
}
