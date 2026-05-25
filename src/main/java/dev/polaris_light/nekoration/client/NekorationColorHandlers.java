package dev.polaris_light.nekoration.client;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.api.block.NekorationBlockStateProperties;
import dev.polaris_light.nekoration.api.item.DyeableBlockItem;
import dev.polaris_light.nekoration.init.block.BlockRegistry;
import dev.polaris_light.nekoration.init.item.ColoredFurnitureItem;
import dev.polaris_light.nekoration.item.FurnitureColor;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = Nekoration.MODID, value = Dist.CLIENT)
public final class NekorationColorHandlers {
    private NekorationColorHandlers() {
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
            (state, level, pos, tintIndex) -> tintIndex >= 0 ? colorFromId(state.getValue(NekorationBlockStateProperties.COLOR)) : -1,
            BlockRegistry.GLASS_TABLE.get(),
            BlockRegistry.GLASS_ROUND_TABLE.get(),
            BlockRegistry.ARM_CHAIR.get(),
            BlockRegistry.BENCH.get(),
            BlockRegistry.DRAWER.get(),
            BlockRegistry.CABINET.get(),
            BlockRegistry.DRAWER_CHEST.get(),
            BlockRegistry.CUPBOARD.get(),
            BlockRegistry.SHELF.get(),
            BlockRegistry.WALL_SHELF.get()
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
            (stack, tintIndex) -> tintIndex >= 0 && stack.getItem() instanceof DyeableBlockItem furnitureBlockItem
                ? furnitureBlockItem.color().color()
                : -1,
            appendDisplayShelfItems()
        );
    }

    private static Item[] appendDisplayShelfItems() {
        return ColoredFurnitureItem.getItems();
    }

    private static int colorFromId(int colorId) {
        return FurnitureColor.byId(colorId).color();
    }
}
