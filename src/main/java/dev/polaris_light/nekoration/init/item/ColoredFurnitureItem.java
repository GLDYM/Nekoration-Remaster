package dev.polaris_light.nekoration.init.item;

import dev.polaris_light.nekoration.api.item.DyeableBlockItem;
import dev.polaris_light.nekoration.init.block.BlockRegistry;
import dev.polaris_light.nekoration.item.FurnitureColor;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public enum ColoredFurnitureItem {
    BLACK_GLASS_TABLE("black_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.BLACK),
    BLUE_GLASS_TABLE("blue_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.BLUE),
    BROWN_GLASS_TABLE("brown_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.BROWN),
    CYAN_GLASS_TABLE("cyan_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.CYAN),
    GRAY_GLASS_TABLE("gray_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.GRAY),
    GREEN_GLASS_TABLE("green_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.GREEN),
    LIGHT_BLUE_GLASS_TABLE("light_blue_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.LIGHT_BLUE),
    LIGHT_GRAY_GLASS_TABLE("light_gray_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.LIGHT_GRAY),
    LIME_GLASS_TABLE("lime_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.LIME),
    MAGENTA_GLASS_TABLE("magenta_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.MAGENTA),
    ORANGE_GLASS_TABLE("orange_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.ORANGE),
    PINK_GLASS_TABLE("pink_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.PINK),
    PURPLE_GLASS_TABLE("purple_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.PURPLE),
    RED_GLASS_TABLE("red_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.RED),
    WHITE_GLASS_TABLE("white_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.WHITE),
    YELLOW_GLASS_TABLE("yellow_glass_table", "glass_table", BlockRegistry.GLASS_TABLE, FurnitureColor.YELLOW),

    BLACK_GLASS_ROUND_TABLE("black_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.BLACK),
    BLUE_GLASS_ROUND_TABLE("blue_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.BLUE),
    BROWN_GLASS_ROUND_TABLE("brown_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.BROWN),
    CYAN_GLASS_ROUND_TABLE("cyan_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.CYAN),
    GRAY_GLASS_ROUND_TABLE("gray_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.GRAY),
    GREEN_GLASS_ROUND_TABLE("green_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.GREEN),
    LIGHT_BLUE_GLASS_ROUND_TABLE("light_blue_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.LIGHT_BLUE),
    LIGHT_GRAY_GLASS_ROUND_TABLE("light_gray_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.LIGHT_GRAY),
    LIME_GLASS_ROUND_TABLE("lime_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.LIME),
    MAGENTA_GLASS_ROUND_TABLE("magenta_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.MAGENTA),
    ORANGE_GLASS_ROUND_TABLE("orange_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.ORANGE),
    PINK_GLASS_ROUND_TABLE("pink_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.PINK),
    PURPLE_GLASS_ROUND_TABLE("purple_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.PURPLE),
    RED_GLASS_ROUND_TABLE("red_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.RED),
    WHITE_GLASS_ROUND_TABLE("white_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.WHITE),
    YELLOW_GLASS_ROUND_TABLE("yellow_glass_round_table", "glass_round_table", BlockRegistry.GLASS_ROUND_TABLE, FurnitureColor.YELLOW),

    BLACK_ARM_CHAIR("black_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.BLACK),
    BLUE_ARM_CHAIR("blue_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.BLUE),
    BROWN_ARM_CHAIR("brown_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.BROWN),
    CYAN_ARM_CHAIR("cyan_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.CYAN),
    GRAY_ARM_CHAIR("gray_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.GRAY),
    GREEN_ARM_CHAIR("green_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.GREEN),
    LIGHT_BLUE_ARM_CHAIR("light_blue_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.LIGHT_BLUE),
    LIGHT_GRAY_ARM_CHAIR("light_gray_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.LIGHT_GRAY),
    LIME_ARM_CHAIR("lime_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.LIME),
    MAGENTA_ARM_CHAIR("magenta_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.MAGENTA),
    ORANGE_ARM_CHAIR("orange_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.ORANGE),
    PINK_ARM_CHAIR("pink_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.PINK),
    PURPLE_ARM_CHAIR("purple_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.PURPLE),
    RED_ARM_CHAIR("red_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.RED),
    WHITE_ARM_CHAIR("white_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.WHITE),
    YELLOW_ARM_CHAIR("yellow_arm_chair", "arm_chair", BlockRegistry.ARM_CHAIR, FurnitureColor.YELLOW),

    BLACK_BENCH("black_bench", "bench", BlockRegistry.BENCH, FurnitureColor.BLACK),
    BLUE_BENCH("blue_bench", "bench", BlockRegistry.BENCH, FurnitureColor.BLUE),
    BROWN_BENCH("brown_bench", "bench", BlockRegistry.BENCH, FurnitureColor.BROWN),
    CYAN_BENCH("cyan_bench", "bench", BlockRegistry.BENCH, FurnitureColor.CYAN),
    GRAY_BENCH("gray_bench", "bench", BlockRegistry.BENCH, FurnitureColor.GRAY),
    GREEN_BENCH("green_bench", "bench", BlockRegistry.BENCH, FurnitureColor.GREEN),
    LIGHT_BLUE_BENCH("light_blue_bench", "bench", BlockRegistry.BENCH, FurnitureColor.LIGHT_BLUE),
    LIGHT_GRAY_BENCH("light_gray_bench", "bench", BlockRegistry.BENCH, FurnitureColor.LIGHT_GRAY),
    LIME_BENCH("lime_bench", "bench", BlockRegistry.BENCH, FurnitureColor.LIME),
    MAGENTA_BENCH("magenta_bench", "bench", BlockRegistry.BENCH, FurnitureColor.MAGENTA),
    ORANGE_BENCH("orange_bench", "bench", BlockRegistry.BENCH, FurnitureColor.ORANGE),
    PINK_BENCH("pink_bench", "bench", BlockRegistry.BENCH, FurnitureColor.PINK),
    PURPLE_BENCH("purple_bench", "bench", BlockRegistry.BENCH, FurnitureColor.PURPLE),
    RED_BENCH("red_bench", "bench", BlockRegistry.BENCH, FurnitureColor.RED),
    WHITE_BENCH("white_bench", "bench", BlockRegistry.BENCH, FurnitureColor.WHITE),
    YELLOW_BENCH("yellow_bench", "bench", BlockRegistry.BENCH, FurnitureColor.YELLOW),

    BLACK_CUPBOARD("black_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.BLACK),
    BLUE_CUPBOARD("blue_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.BLUE),
    BROWN_CUPBOARD("brown_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.BROWN),
    CYAN_CUPBOARD("cyan_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.CYAN),
    GRAY_CUPBOARD("gray_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.GRAY),
    GREEN_CUPBOARD("green_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.GREEN),
    LIGHT_BLUE_CUPBOARD("light_blue_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.LIGHT_BLUE),
    LIGHT_GRAY_CUPBOARD("light_gray_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.LIGHT_GRAY),
    LIME_CUPBOARD("lime_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.LIME),
    MAGENTA_CUPBOARD("magenta_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.MAGENTA),
    ORANGE_CUPBOARD("orange_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.ORANGE),
    PINK_CUPBOARD("pink_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.PINK),
    PURPLE_CUPBOARD("purple_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.PURPLE),
    RED_CUPBOARD("red_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.RED),
    WHITE_CUPBOARD("white_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.WHITE),
    YELLOW_CUPBOARD("yellow_cupboard", "cupboard", BlockRegistry.CUPBOARD, FurnitureColor.YELLOW),

    BLACK_SHELF("black_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.BLACK),
    BLUE_SHELF("blue_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.BLUE),
    BROWN_SHELF("brown_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.BROWN),
    CYAN_SHELF("cyan_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.CYAN),
    GRAY_SHELF("gray_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.GRAY),
    GREEN_SHELF("green_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.GREEN),
    LIGHT_BLUE_SHELF("light_blue_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.LIGHT_BLUE),
    LIGHT_GRAY_SHELF("light_gray_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.LIGHT_GRAY),
    LIME_SHELF("lime_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.LIME),
    MAGENTA_SHELF("magenta_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.MAGENTA),
    ORANGE_SHELF("orange_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.ORANGE),
    PINK_SHELF("pink_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.PINK),
    PURPLE_SHELF("purple_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.PURPLE),
    RED_SHELF("red_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.RED),
    WHITE_SHELF("white_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.WHITE),
    YELLOW_SHELF("yellow_shelf", "shelf", BlockRegistry.SHELF, FurnitureColor.YELLOW),

    BLACK_WALL_SHELF("black_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.BLACK),
    BLUE_WALL_SHELF("blue_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.BLUE),
    BROWN_WALL_SHELF("brown_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.BROWN),
    CYAN_WALL_SHELF("cyan_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.CYAN),
    GRAY_WALL_SHELF("gray_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.GRAY),
    GREEN_WALL_SHELF("green_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.GREEN),
    LIGHT_BLUE_WALL_SHELF("light_blue_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.LIGHT_BLUE),
    LIGHT_GRAY_WALL_SHELF("light_gray_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.LIGHT_GRAY),
    LIME_WALL_SHELF("lime_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.LIME),
    MAGENTA_WALL_SHELF("magenta_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.MAGENTA),
    ORANGE_WALL_SHELF("orange_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.ORANGE),
    PINK_WALL_SHELF("pink_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.PINK),
    PURPLE_WALL_SHELF("purple_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.PURPLE),
    RED_WALL_SHELF("red_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.RED),
    WHITE_WALL_SHELF("white_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.WHITE),
    YELLOW_WALL_SHELF("yellow_wall_shelf", "wall_shelf", BlockRegistry.WALL_SHELF, FurnitureColor.YELLOW);

    private static final Map<String, Map<FurnitureColor, ColoredFurnitureItem>> BY_NAME_AND_COLOR = new HashMap<>();
    private static final Map<Item, ColoredFurnitureItem> BY_ITEM = new HashMap<>();

    static {
        for (ColoredFurnitureItem entry : values()) {
            BY_NAME_AND_COLOR.computeIfAbsent(entry.furnitureName, ignored -> new EnumMap<>(FurnitureColor.class)).put(entry.color, entry);
        }
    }

    private final String registryName;
    private final String furnitureName;
    private final Supplier<? extends Block> block;
    private final FurnitureColor color;
    private DeferredItem<Item> item;

    ColoredFurnitureItem(String registryName, String furnitureName, Supplier<? extends Block> block, FurnitureColor color) {
        this.registryName = registryName;
        this.furnitureName = furnitureName;
        this.block = block;
        this.color = color;
    }

    public String getRegistryName() {
        return this.registryName;
    }

    public Supplier<? extends Block> getBlock() {
        return this.block;
    }

    public String getFurnitureName() {
        return this.furnitureName;
    }

    public FurnitureColor getColor() {
        return this.color;
    }

    public DeferredItem<Item> register(DeferredRegister.Items items) {
        this.item = items.register(
            this.registryName,
            () -> new DyeableBlockItem(this.block.get(), dev.polaris_light.nekoration.Nekoration.defaultItemProperties(), this.color)
        );
        return this.item;
    }

    public DeferredItem<Item> getDeferredItem() {
        return this.item;
    }

    public Item getItem() {
        return this.item.get();
    }

    public ItemStack getItemStack() {
        return new ItemStack(this.getItem());
    }

    public static ColoredFurnitureItem get(String name, FurnitureColor color) {
        return BY_NAME_AND_COLOR.get(name).get(color);
    }

    public static ColoredFurnitureItem get(Item item) {
        if (BY_ITEM.isEmpty()) {
            for (ColoredFurnitureItem value : values()) {
                BY_ITEM.put(value.getItem(), value);
            }
        }
        return BY_ITEM.get(item);
    }

    public static Item[] getItems() {
        List<Item> items = new ArrayList<>();
        for (ColoredFurnitureItem value : values()) {
            items.add(value.getItem());
        }
        return items.toArray(Item[]::new);
    }
}
