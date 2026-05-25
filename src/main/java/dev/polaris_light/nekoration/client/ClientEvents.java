package dev.polaris_light.nekoration.client;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.client.render.DisplayShelfBlockEntityRenderer;
import dev.polaris_light.nekoration.client.render.SeatEntityRenderer;
import dev.polaris_light.nekoration.init.EntityTypeRegistry;
import dev.polaris_light.nekoration.init.block.BlockEntityRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Nekoration.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(BlockEntityRegistry.DISPLAY_SHELF.get(), DisplayShelfBlockEntityRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.SEAT.get(), SeatEntityRenderer::new);
        });
    }
}
