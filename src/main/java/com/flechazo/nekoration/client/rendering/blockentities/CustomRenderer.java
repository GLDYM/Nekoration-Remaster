package com.flechazo.nekoration.client.rendering.blockentities;

import com.flechazo.nekoration.blocks.ModBlocks;
import com.flechazo.nekoration.blocks.entities.CustomBlockEntity;
import com.flechazo.nekoration.items.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.HashMap;
import java.util.Map;

public class CustomRenderer implements BlockEntityRenderer<CustomBlockEntity> {
    private static final Map<Level, CustomRendererTintGetter> tintGetters = new HashMap<>();
    private static final ItemStack ARROW_HINT = new ItemStack(ModItems.ARROW_HINT.get());
    private static final double FRAC = 1.0 / 32.0;

    public CustomRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(CustomBlockEntity tileEntity, float partialTicks, PoseStack stack, MultiBufferSource buffers, int combinedLight, int combinedOverlay) {
        stack.pushPose();

        applyTransformations(tileEntity, stack);

        if (tileEntity.showHint) {
            renderHintArrow(stack, buffers);
        }

        renderBlockModel(tileEntity, stack, buffers);

        stack.popPose();
    }

    private void applyTransformations(CustomBlockEntity tileEntity, PoseStack stack) {
        stack.translate(0.5, 0.0, 0.5);
        stack.mulPose(Axis.YP.rotationDegrees(tileEntity.dir * 15F));
        stack.translate(tileEntity.offset[0] * FRAC, tileEntity.offset[1] * FRAC, tileEntity.offset[2] * FRAC);
    }

    private void renderHintArrow(PoseStack stack, MultiBufferSource buffers) {
        stack.translate(0.0, 0.5, -1.0);
        stack.mulPose(Axis.XP.rotationDegrees(90F));

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = itemRenderer.getModel(ARROW_HINT, null, null, 0);
        itemRenderer.render(ARROW_HINT, ItemDisplayContext.FIXED, true, stack, buffers, 255, 0, model);

        stack.mulPose(Axis.XP.rotationDegrees(-90F));
        stack.translate(0.0, -0.5, 1.0);
    }

    private void renderBlockModel(CustomBlockEntity tileEntity, PoseStack stack, MultiBufferSource buffers) {
        stack.translate(-0.5, 0.0, -0.5);

        BlockState state = tileEntity.displayState != CustomBlockEntity.defaultState
                ? tileEntity.displayState
                : ModBlocks.DREAM_WAS_TAKEN.get().defaultBlockState();

        Level level = tileEntity.getLevel();
        BlockPos pos = tileEntity.getBlockPos();
        long seed = state.getSeed(pos);
        RandomSource random = RandomSource.create(seed);

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = dispatcher.getBlockModel(state);

        for (RenderType type : model.getRenderTypes(state, random, ModelData.EMPTY)) {
            VertexConsumer buffer = buffers.getBuffer(type);

            if (tileEntity.retint) {
                int rgb = (tileEntity.color[0] << 16) | (tileEntity.color[1] << 8) | tileEntity.color[2];
                CustomRendererTintGetter tintGetter = tintGetters.computeIfAbsent(level, CustomRendererTintGetter::new);
                tintGetter.SetCustomTint(rgb);

                dispatcher.getModelRenderer().tesselateBlock(tintGetter, model, state, pos, stack, buffer, false, random, seed, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, type);
            } else {
                dispatcher.getModelRenderer().tesselateBlock(level, model, state, pos, stack, buffer, false, random, seed, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, type);
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(CustomBlockEntity tileEntity) {
        return true;
    }
}
