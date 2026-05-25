package dev.polaris_light.nekoration.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.polaris_light.nekoration.entity.SeatEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public final class SeatEntityRenderer extends EntityRenderer<SeatEntity> {
    public SeatEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
        SeatEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight
    ) {
    }

    @Override
    public ResourceLocation getTextureLocation(SeatEntity entity) {
        return ResourceLocation.withDefaultNamespace("textures/misc/empty.png");
    }
}
