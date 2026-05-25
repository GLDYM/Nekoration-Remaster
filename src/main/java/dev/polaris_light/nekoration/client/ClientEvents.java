package dev.polaris_light.nekoration.client;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.client.render.SeatEntityRenderer;
import dev.polaris_light.nekoration.init.EntityTypeRegistry;
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
        event.enqueueWork(() -> EntityRenderers.register(EntityTypeRegistry.SEAT.get(), SeatEntityRenderer::new));
    }
}
