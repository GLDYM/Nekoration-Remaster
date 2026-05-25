package com.flechazo.nekoration.utils;

import com.flechazo.nekoration.blocks.ModBlocks;
import net.minecraft.world.item.ItemStack;

public class ItemIconHelper {
    public static ItemStack getCustomBlockItem(int customModelData) {
        ItemStack item = new ItemStack(ModBlocks.CUSTOM.get());
        item.getOrCreateTag().putInt("CustomModelData", customModelData);
        return item;
    }
}