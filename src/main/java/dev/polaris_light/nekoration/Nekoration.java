package dev.polaris_light.nekoration;

import dev.polaris_light.nekoration.init.CreativeModeTabRegistry;
import dev.polaris_light.nekoration.init.EntityTypeRegistry;
import dev.polaris_light.nekoration.init.block.BlockRegistry;
import dev.polaris_light.nekoration.init.item.BlockItemRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Nekoration.MODID)
public final class Nekoration {
    public static final String MODID = "nekoration";

    public Nekoration(IEventBus modEventBus, ModContainer modContainer) {
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockItemRegistry.ITEMS.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        CreativeModeTabRegistry.TABS.register(modEventBus);
    }

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties();
    }
}
