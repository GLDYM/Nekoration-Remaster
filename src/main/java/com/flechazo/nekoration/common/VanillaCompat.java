package com.flechazo.nekoration.common;

import com.flechazo.nekoration.blocks.ModBlocks;
import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class VanillaCompat {
    private static final Logger LOGGER = LogManager.getLogger("Vanilla Compat");
    public static final Map<Item, Integer> COLOR_ITEMS = Maps.newHashMap();
    public static final Map<Item, Integer> RAW_COLOR_ITEMS = Maps.newHashMap();
    public static final Map<Item, Integer> FLAME_ITEMS = Maps.newHashMap();

    private record ItemEntry(Item item, int id) {
    }

    public static void Initialize() {
        for (var e : new ItemEntry[]{
                new ItemEntry(Items.BLACK_DYE, 0),
                new ItemEntry(Items.BLUE_DYE, 1),
                new ItemEntry(Items.BROWN_DYE, 2),
                new ItemEntry(Items.CYAN_DYE, 3),
                new ItemEntry(Items.GRAY_DYE, 4),
                new ItemEntry(Items.GREEN_DYE, 5),
                new ItemEntry(Items.LIGHT_BLUE_DYE, 6),
                new ItemEntry(Items.LIGHT_GRAY_DYE, 7),
                new ItemEntry(Items.LIME_DYE, 8),
                new ItemEntry(Items.MAGENTA_DYE, 9),
                new ItemEntry(Items.ORANGE_DYE, 10),
                new ItemEntry(Items.PINK_DYE, 11),
                new ItemEntry(Items.PURPLE_DYE, 12),
                new ItemEntry(Items.RED_DYE, 13),
                new ItemEntry(Items.WHITE_DYE, 14),
                new ItemEntry(Items.YELLOW_DYE, 15),
        }) {
            COLOR_ITEMS.put(e.item, e.id);
        }

        for (var e : new ItemEntry[]{
                new ItemEntry(Items.INK_SAC, 0),
                new ItemEntry(Items.WITHER_ROSE, 0),
                new ItemEntry(Items.LAPIS_LAZULI, 1),
                new ItemEntry(Items.COCOA_BEANS, 2),
                new ItemEntry(Items.CORNFLOWER, 3),
                new ItemEntry(Items.STONE, 4),
                new ItemEntry(Items.COBBLESTONE, 4),
                new ItemEntry(Items.CACTUS, 5),
                new ItemEntry(Items.BLUE_ORCHID, 6),
                new ItemEntry(Items.AZURE_BLUET, 7),
                new ItemEntry(Items.OXEYE_DAISY, 7),
                new ItemEntry(Items.WHITE_TULIP, 7),
                new ItemEntry(Items.SEA_PICKLE, 8),
                new ItemEntry(Items.LILAC, 9),
                new ItemEntry(Items.ORANGE_TULIP, 10),
                new ItemEntry(Items.PEONY, 11),
                new ItemEntry(Items.PINK_TULIP, 11),
                new ItemEntry(Items.ALLIUM, 12),
                new ItemEntry(Items.POPPY, 13),
                new ItemEntry(Items.ROSE_BUSH, 13),
                new ItemEntry(Items.RED_TULIP, 13),
                new ItemEntry(Items.BEETROOT, 13),
                new ItemEntry(Items.POPPY, 13),
                new ItemEntry(Items.BONE_MEAL, 14),
                new ItemEntry(Items.LILY_OF_THE_VALLEY, 14),
                new ItemEntry(Items.DANDELION, 15),
                new ItemEntry(Items.SUNFLOWER, 15),
        }) {
            RAW_COLOR_ITEMS.put(e.item, e.id);
        }

        for (var e : new ItemEntry[]{
                new ItemEntry(Items.TORCH, 1),
                new ItemEntry(Items.FLINT_AND_STEEL, 1),
                new ItemEntry(Items.LANTERN, 1),
                new ItemEntry(Items.CAMPFIRE, 1),
                new ItemEntry(Items.SOUL_TORCH, 2),
                new ItemEntry(Items.SOUL_LANTERN, 2),
                new ItemEntry(Items.SOUL_CAMPFIRE, 2),
                new ItemEntry(Items.NETHER_STAR, 3),
                new ItemEntry(Items.BEACON, 3),
                new ItemEntry(Items.END_CRYSTAL, 3),
        }) {
            FLAME_ITEMS.put(e.item, e.id);
        }

        registerFlammablity();

        LOGGER.debug("Vanilla Compat Initialized!");
    }

    @SuppressWarnings("unchecked")
    private static void registerFlammablity() {
        FireBlock fire = (FireBlock) Blocks.FIRE;

        RegistryObject<Block>[] flammable = new RegistryObject[]{
                // Half-Timber
                ModBlocks.HALF_TIMBER_P0, ModBlocks.HALF_TIMBER_P1, ModBlocks.HALF_TIMBER_P2,
                ModBlocks.HALF_TIMBER_P3, ModBlocks.HALF_TIMBER_P4, ModBlocks.HALF_TIMBER_P5,
                ModBlocks.HALF_TIMBER_P6, ModBlocks.HALF_TIMBER_P7, ModBlocks.HALF_TIMBER_P8,
                ModBlocks.HALF_TIMBER_P9,
                ModBlocks.HALF_TIMBER_PILLAR_P0, ModBlocks.HALF_TIMBER_PILLAR_P1, ModBlocks.HALF_TIMBER_PILLAR_P2,
                // Furniture
                ModBlocks.PUMPKIN_CHAIR, ModBlocks.PUMPKIN_TABLE,
                ModBlocks.OAK_CHAIR, ModBlocks.OAK_TABLE, ModBlocks.OAK_ROUND_TABLE,
                ModBlocks.JUNGLE_CHAIR, ModBlocks.JUNGLE_TABLE, ModBlocks.JUNGLE_ROUND_TABLE,
                ModBlocks.ACACIA_CHAIR, ModBlocks.ACACIA_TABLE, ModBlocks.ACACIA_ROUND_TABLE,
                ModBlocks.BIRCH_CHAIR, ModBlocks.BIRCH_TABLE, ModBlocks.BIRCH_ROUND_TABLE,
                ModBlocks.DARK_OAK_CHAIR, ModBlocks.DARK_OAK_TABLE, ModBlocks.DARK_OAK_ROUND_TABLE,
                ModBlocks.SPRUCE_CHAIR, ModBlocks.SPRUCE_TABLE, ModBlocks.SPRUCE_ROUND_TABLE,
                ModBlocks.MANGROVE_CHAIR, ModBlocks.MANGROVE_TABLE, ModBlocks.MANGROVE_ROUND_TABLE,
                ModBlocks.GLASS_TABLE, ModBlocks.GLASS_ROUND_TABLE, ModBlocks.ARM_CHAIR, ModBlocks.BENCH,
                ModBlocks.DRAWER, ModBlocks.CABINET, ModBlocks.DRAWER_CHEST,
                ModBlocks.CUPBOARD, ModBlocks.SHELF, ModBlocks.WALL_SHELF,
                // Windows
                ModBlocks.WINDOW_SIMPLE, ModBlocks.WINDOW_ARCH, ModBlocks.WINDOW_CROSS,
                ModBlocks.WINDOW_LANCET, ModBlocks.WINDOW_SHADE,
                ModBlocks.WINDOW_SILL, ModBlocks.WINDOW_TOP, ModBlocks.WINDOW_PLANT,
                // Awnings
                ModBlocks.AWNING_PURE, ModBlocks.AWNING_PURE_SHORT,
                ModBlocks.AWNING_STRIPE, ModBlocks.AWNING_STRIPE_SHORT,
                // Easel Menus
                ModBlocks.EASEL_MENU, ModBlocks.EASEL_MENU_WHITE,
        };

        for (RegistryObject<Block> regObj : flammable) {
            fire.setFlammable(regObj.get(), 5, 20);
        }
    }
}