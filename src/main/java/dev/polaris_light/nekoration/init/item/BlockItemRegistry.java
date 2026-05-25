package dev.polaris_light.nekoration.init.item;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.init.block.BlockRegistry;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BlockItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Nekoration.MODID);

    public static final DeferredItem<Item> OAK_CHAIR = register("oak_chair", BlockRegistry.OAK_CHAIR);
    public static final DeferredItem<Item> SPRUCE_CHAIR = register("spruce_chair", BlockRegistry.SPRUCE_CHAIR);
    public static final DeferredItem<Item> BIRCH_CHAIR = register("birch_chair", BlockRegistry.BIRCH_CHAIR);
    public static final DeferredItem<Item> JUNGLE_CHAIR = register("jungle_chair", BlockRegistry.JUNGLE_CHAIR);
    public static final DeferredItem<Item> ACACIA_CHAIR = register("acacia_chair", BlockRegistry.ACACIA_CHAIR);
    public static final DeferredItem<Item> DARK_OAK_CHAIR = register("dark_oak_chair", BlockRegistry.DARK_OAK_CHAIR);
    public static final DeferredItem<Item> CRIMSON_CHAIR = register("crimson_chair", BlockRegistry.CRIMSON_CHAIR);
    public static final DeferredItem<Item> WARPED_CHAIR = register("warped_chair", BlockRegistry.WARPED_CHAIR);
    public static final DeferredItem<Item> MANGROVE_CHAIR = register("mangrove_chair", BlockRegistry.MANGROVE_CHAIR);
    public static final DeferredItem<Item> PUMPKIN_CHAIR = register("pumpkin_chair", BlockRegistry.PUMPKIN_CHAIR);
    public static final DeferredItem<Item> OAK_TABLE = register("oak_table", BlockRegistry.OAK_TABLE);
    public static final DeferredItem<Item> SPRUCE_TABLE = register("spruce_table", BlockRegistry.SPRUCE_TABLE);
    public static final DeferredItem<Item> BIRCH_TABLE = register("birch_table", BlockRegistry.BIRCH_TABLE);
    public static final DeferredItem<Item> JUNGLE_TABLE = register("jungle_table", BlockRegistry.JUNGLE_TABLE);
    public static final DeferredItem<Item> ACACIA_TABLE = register("acacia_table", BlockRegistry.ACACIA_TABLE);
    public static final DeferredItem<Item> DARK_OAK_TABLE = register("dark_oak_table", BlockRegistry.DARK_OAK_TABLE);
    public static final DeferredItem<Item> CRIMSON_TABLE = register("crimson_table", BlockRegistry.CRIMSON_TABLE);
    public static final DeferredItem<Item> WARPED_TABLE = register("warped_table", BlockRegistry.WARPED_TABLE);
    public static final DeferredItem<Item> MANGROVE_TABLE = register("mangrove_table", BlockRegistry.MANGROVE_TABLE);
    public static final DeferredItem<Item> PUMPKIN_TABLE = register("pumpkin_table", BlockRegistry.PUMPKIN_TABLE);
    public static final DeferredItem<Item> OAK_ROUND_TABLE = register("oak_round_table", BlockRegistry.OAK_ROUND_TABLE);
    public static final DeferredItem<Item> SPRUCE_ROUND_TABLE = register("spruce_round_table", BlockRegistry.SPRUCE_ROUND_TABLE);
    public static final DeferredItem<Item> BIRCH_ROUND_TABLE = register("birch_round_table", BlockRegistry.BIRCH_ROUND_TABLE);
    public static final DeferredItem<Item> JUNGLE_ROUND_TABLE = register("jungle_round_table", BlockRegistry.JUNGLE_ROUND_TABLE);
    public static final DeferredItem<Item> ACACIA_ROUND_TABLE = register("acacia_round_table", BlockRegistry.ACACIA_ROUND_TABLE);
    public static final DeferredItem<Item> DARK_OAK_ROUND_TABLE = register("dark_oak_round_table", BlockRegistry.DARK_OAK_ROUND_TABLE);
    public static final DeferredItem<Item> CRIMSON_ROUND_TABLE = register("crimson_round_table", BlockRegistry.CRIMSON_ROUND_TABLE);
    public static final DeferredItem<Item> WARPED_ROUND_TABLE = register("warped_round_table", BlockRegistry.WARPED_ROUND_TABLE);
    public static final DeferredItem<Item> MANGROVE_ROUND_TABLE = register("mangrove_round_table", BlockRegistry.MANGROVE_ROUND_TABLE);

    public static final DeferredItem<Item> BLACK_GLASS_TABLE = register(ColoredFurnitureItem.BLACK_GLASS_TABLE);
    public static final DeferredItem<Item> BLUE_GLASS_TABLE = register(ColoredFurnitureItem.BLUE_GLASS_TABLE);
    public static final DeferredItem<Item> BROWN_GLASS_TABLE = register(ColoredFurnitureItem.BROWN_GLASS_TABLE);
    public static final DeferredItem<Item> CYAN_GLASS_TABLE = register(ColoredFurnitureItem.CYAN_GLASS_TABLE);
    public static final DeferredItem<Item> GRAY_GLASS_TABLE = register(ColoredFurnitureItem.GRAY_GLASS_TABLE);
    public static final DeferredItem<Item> GREEN_GLASS_TABLE = register(ColoredFurnitureItem.GREEN_GLASS_TABLE);
    public static final DeferredItem<Item> LIGHT_BLUE_GLASS_TABLE = register(ColoredFurnitureItem.LIGHT_BLUE_GLASS_TABLE);
    public static final DeferredItem<Item> LIGHT_GRAY_GLASS_TABLE = register(ColoredFurnitureItem.LIGHT_GRAY_GLASS_TABLE);
    public static final DeferredItem<Item> LIME_GLASS_TABLE = register(ColoredFurnitureItem.LIME_GLASS_TABLE);
    public static final DeferredItem<Item> MAGENTA_GLASS_TABLE = register(ColoredFurnitureItem.MAGENTA_GLASS_TABLE);
    public static final DeferredItem<Item> ORANGE_GLASS_TABLE = register(ColoredFurnitureItem.ORANGE_GLASS_TABLE);
    public static final DeferredItem<Item> PINK_GLASS_TABLE = register(ColoredFurnitureItem.PINK_GLASS_TABLE);
    public static final DeferredItem<Item> PURPLE_GLASS_TABLE = register(ColoredFurnitureItem.PURPLE_GLASS_TABLE);
    public static final DeferredItem<Item> RED_GLASS_TABLE = register(ColoredFurnitureItem.RED_GLASS_TABLE);
    public static final DeferredItem<Item> WHITE_GLASS_TABLE = register(ColoredFurnitureItem.WHITE_GLASS_TABLE);
    public static final DeferredItem<Item> YELLOW_GLASS_TABLE = register(ColoredFurnitureItem.YELLOW_GLASS_TABLE);

    public static final DeferredItem<Item> BLACK_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.BLACK_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> BLUE_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.BLUE_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> BROWN_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.BROWN_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> CYAN_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.CYAN_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> GRAY_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.GRAY_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> GREEN_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.GREEN_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> LIGHT_BLUE_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.LIGHT_BLUE_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> LIGHT_GRAY_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.LIGHT_GRAY_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> LIME_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.LIME_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> MAGENTA_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.MAGENTA_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> ORANGE_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.ORANGE_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> PINK_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.PINK_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> PURPLE_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.PURPLE_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> RED_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.RED_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> WHITE_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.WHITE_GLASS_ROUND_TABLE);
    public static final DeferredItem<Item> YELLOW_GLASS_ROUND_TABLE = register(ColoredFurnitureItem.YELLOW_GLASS_ROUND_TABLE);

    public static final DeferredItem<Item> BLACK_ARM_CHAIR = register(ColoredFurnitureItem.BLACK_ARM_CHAIR);
    public static final DeferredItem<Item> BLUE_ARM_CHAIR = register(ColoredFurnitureItem.BLUE_ARM_CHAIR);
    public static final DeferredItem<Item> BROWN_ARM_CHAIR = register(ColoredFurnitureItem.BROWN_ARM_CHAIR);
    public static final DeferredItem<Item> CYAN_ARM_CHAIR = register(ColoredFurnitureItem.CYAN_ARM_CHAIR);
    public static final DeferredItem<Item> GRAY_ARM_CHAIR = register(ColoredFurnitureItem.GRAY_ARM_CHAIR);
    public static final DeferredItem<Item> GREEN_ARM_CHAIR = register(ColoredFurnitureItem.GREEN_ARM_CHAIR);
    public static final DeferredItem<Item> LIGHT_BLUE_ARM_CHAIR = register(ColoredFurnitureItem.LIGHT_BLUE_ARM_CHAIR);
    public static final DeferredItem<Item> LIGHT_GRAY_ARM_CHAIR = register(ColoredFurnitureItem.LIGHT_GRAY_ARM_CHAIR);
    public static final DeferredItem<Item> LIME_ARM_CHAIR = register(ColoredFurnitureItem.LIME_ARM_CHAIR);
    public static final DeferredItem<Item> MAGENTA_ARM_CHAIR = register(ColoredFurnitureItem.MAGENTA_ARM_CHAIR);
    public static final DeferredItem<Item> ORANGE_ARM_CHAIR = register(ColoredFurnitureItem.ORANGE_ARM_CHAIR);
    public static final DeferredItem<Item> PINK_ARM_CHAIR = register(ColoredFurnitureItem.PINK_ARM_CHAIR);
    public static final DeferredItem<Item> PURPLE_ARM_CHAIR = register(ColoredFurnitureItem.PURPLE_ARM_CHAIR);
    public static final DeferredItem<Item> RED_ARM_CHAIR = register(ColoredFurnitureItem.RED_ARM_CHAIR);
    public static final DeferredItem<Item> WHITE_ARM_CHAIR = register(ColoredFurnitureItem.WHITE_ARM_CHAIR);
    public static final DeferredItem<Item> YELLOW_ARM_CHAIR = register(ColoredFurnitureItem.YELLOW_ARM_CHAIR);

    public static final DeferredItem<Item> BLACK_BENCH = register(ColoredFurnitureItem.BLACK_BENCH);
    public static final DeferredItem<Item> BLUE_BENCH = register(ColoredFurnitureItem.BLUE_BENCH);
    public static final DeferredItem<Item> BROWN_BENCH = register(ColoredFurnitureItem.BROWN_BENCH);
    public static final DeferredItem<Item> CYAN_BENCH = register(ColoredFurnitureItem.CYAN_BENCH);
    public static final DeferredItem<Item> GRAY_BENCH = register(ColoredFurnitureItem.GRAY_BENCH);
    public static final DeferredItem<Item> GREEN_BENCH = register(ColoredFurnitureItem.GREEN_BENCH);
    public static final DeferredItem<Item> LIGHT_BLUE_BENCH = register(ColoredFurnitureItem.LIGHT_BLUE_BENCH);
    public static final DeferredItem<Item> LIGHT_GRAY_BENCH = register(ColoredFurnitureItem.LIGHT_GRAY_BENCH);
    public static final DeferredItem<Item> LIME_BENCH = register(ColoredFurnitureItem.LIME_BENCH);
    public static final DeferredItem<Item> MAGENTA_BENCH = register(ColoredFurnitureItem.MAGENTA_BENCH);
    public static final DeferredItem<Item> ORANGE_BENCH = register(ColoredFurnitureItem.ORANGE_BENCH);
    public static final DeferredItem<Item> PINK_BENCH = register(ColoredFurnitureItem.PINK_BENCH);
    public static final DeferredItem<Item> PURPLE_BENCH = register(ColoredFurnitureItem.PURPLE_BENCH);
    public static final DeferredItem<Item> RED_BENCH = register(ColoredFurnitureItem.RED_BENCH);
    public static final DeferredItem<Item> WHITE_BENCH = register(ColoredFurnitureItem.WHITE_BENCH);
    public static final DeferredItem<Item> YELLOW_BENCH = register(ColoredFurnitureItem.YELLOW_BENCH);

    private static DeferredItem<Item> register(String name, Supplier<? extends Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), Nekoration.defaultItemProperties()));
    }

    private static DeferredItem<Item> register(ColoredFurnitureItem item) {
        return item.register(ITEMS);
    }

    private BlockItemRegistry() {
    }
}
