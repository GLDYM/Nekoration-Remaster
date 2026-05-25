package dev.polaris_light.nekoration.api.item;

import dev.polaris_light.nekoration.item.FurnitureColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class DyeableBlockItem extends BlockItem {
    private final FurnitureColor color;

    public DyeableBlockItem(Block block, Properties properties, FurnitureColor color) {
        super(block, properties);
        this.color = color;
    }

    public FurnitureColor color() {
        return this.color;
    }
}
