package com.flechazo.nekoration.client.event;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.Nekoration;
import com.flechazo.nekoration.blocks.*;
import com.flechazo.nekoration.blocks.containers.ModMenuType;
import com.flechazo.nekoration.blocks.entities.ModBlockEntityType;
import com.flechazo.nekoration.client.gui.screen.EaselMenuScreen;
import com.flechazo.nekoration.client.rendering.blockentities.*;
import com.flechazo.nekoration.client.rendering.entities.PaintingRenderer;
import com.flechazo.nekoration.client.rendering.entities.SeatRenderer;
import com.flechazo.nekoration.client.rendering.entities.WallPaperRenderer;
import com.flechazo.nekoration.entities.ModEntityType;
import com.flechazo.nekoration.items.DyeableBlockItem;
import com.flechazo.nekoration.items.DyeableWoodenBlockItem;
import com.flechazo.nekoration.items.HalfTimberBlockItem;
import com.flechazo.nekoration.items.PaintingItem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.function.ToIntFunction;

@EventBusSubscriber(modid = Nekoration.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventSubscriber {
    private static final Logger LOGGER = LogManager.getLogger("Client Mod Event Subscriber");

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        LOGGER.info("Client Side Setup.");

        BlockEntityRenderers.register(ModBlockEntityType.EASEL_MENU_TYPE.get(), EaselMenuRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityType.CUSTOM_TYPE.get(), CustomRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityType.PHONOGRAPH_TYPE.get(), PhonographRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityType.PRISMAP_TABLE_TYPE.get(), PrismapTableRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityType.ITEM_DISPLAY_TYPE.get(), CupboardRenderer::new);
        LOGGER.info("BlockEntities Renderers Bound.");

        EntityRenderers.register(ModEntityType.PAINTING_TYPE.get(), PaintingRenderer::new);
        EntityRenderers.register(ModEntityType.WALLPAPER_TYPE.get(), WallPaperRenderer::new);
        EntityRenderers.register(ModEntityType.SEAT_TYPE.get(), SeatRenderer::new);
        LOGGER.info("Then Entities Renderers Bound.");

        MinecraftForge.EVENT_BUS.register(new CreativeInventoryEvents());
        LOGGER.info("CreativeInv Events Registered.");

        event.enqueueWork(ClientModEventSubscriber::registerPropertyOverrides);
        LOGGER.info("Property Overrides Registered.");

        MenuScreens.register(ModMenuType.EASEL_MENU_TYPE.get(), EaselMenuScreen::new);
        LOGGER.info("Nekoration Screens Registered.");

        exportMapColors();
    }

    // --- Debug exports ---

    private record SimpleColor(int r, int g, int b) {
    }

    public static void exportMapColors() {
        final var exportFile = new File(Minecraft.getInstance().gameDirectory, "map_colors.json");
        try {
            Field colorsField = ObfuscationReflectionHelper.findField(MapColor.class, "f_76387_");
            colorsField.setAccessible(true);
            var colorDict = (MapColor[]) colorsField.get(null);
            var colorMap = new java.util.HashMap<Integer, SimpleColor>();
            var gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();

            for (var matColor : colorDict) {
                if (matColor != null) {
                    colorMap.put(matColor.id, new SimpleColor(
                            matColor.col >> 16 & 0xFF,
                            matColor.col >> 8 & 0xFF,
                            matColor.col & 0xFF));
                }
            }
            if (exportFile.exists() || exportFile.createNewFile()) {
                var writer = new FileWriter(exportFile);
                writer.write(gson.toJson(colorMap));
                writer.close();
                LOGGER.info("Successfully exported map colors.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to export map colors: {}", e.getMessage());
        }
    }

    // --- Property overrides ---

    public static void registerPropertyOverrides() {
        String[] awnings = {"awning_pure", "awning_stripe", "awning_pure_short", "awning_stripe_short"};
        for (String name : awnings) {
            ItemProperties.register(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Nekoration.MODID, name)),
                    new ResourceLocation("color"), DyeableBlockItem::getColorPropertyOverride);
        }
        ItemProperties.register(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Nekoration.MODID, "painting")),
                new ResourceLocation("type"), PaintingItem::getTypePropertyOverride);
    }

    // --- Block color helpers ---

    private static BlockColor simpleBlockColor(
            ToIntFunction<BlockState> getter, int fallback) {
        return (state, view, pos, tintIndex) ->
                view == null || pos == null ? fallback : getter.applyAsInt(state);
    }

    private static BlockColor tintedBlockColor(
            ToIntFunction<BlockState> getter0,
            ToIntFunction<BlockState> getter1, int fallback0, int fallback1) {
        return (state, view, pos, tintIndex) -> {
            if (view == null || pos == null)
                return tintIndex == 0 ? fallback0 : fallback1;
            return tintIndex == 0 ? getter0.applyAsInt(state) : getter1.applyAsInt(state);
        };
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        // Stone blocks — default light gray (7)
        var stoneColor = simpleBlockColor(
                s -> NekoColors.getStoneColorOrLightGray(s.getValue(DyeableBlock.COLOR)),
                NekoColors.getStoneColorOrLightGray(7));
        event.register(stoneColor,
                ModBlocks.STONE_BASE_BOTTOM.get(), ModBlocks.STONE_FRAME_BOTTOM.get(),
                ModBlocks.STONE_PILLAR_BOTTOM.get(), ModBlocks.STONE_LAYERED.get(),
                ModBlocks.STONE_POT.get(), ModBlocks.STONE_PLANTER.get(),
                ModBlocks.STONE_BOTTOM_THIN.get(), ModBlocks.STONE_PILLAR_THIN.get(),
                ModBlocks.STONE_DORIC_THIN.get(), ModBlocks.STONE_IONIC_THIN.get(),
                ModBlocks.STONE_CORINTHIAN_THIN.get());

        // Stone pillars — default light gray (7)
        var stonePillarColor = simpleBlockColor(
                s -> NekoColors.getStoneColorOrLightGray(s.getValue(DyeableVerticalConnectBlock.COLOR)),
                NekoColors.getStoneColorOrLightGray(7));
        event.register(stonePillarColor,
                ModBlocks.STONE_BASE.get(), ModBlocks.STONE_FRAME.get(), ModBlocks.STONE_PILLAR.get(),
                ModBlocks.STONE_DORIC.get(), ModBlocks.STONE_IONIC.get(), ModBlocks.STONE_CORINTHIAN.get());

        // Candle holders — default white (14)
        var candleColor = simpleBlockColor(
                s -> NekoColors.getNekoColorOrWhite(s.getValue(DyeableBlock.COLOR)),
                NekoColors.getNekoColorOrWhite(14));
        event.register(candleColor,
                ModBlocks.CANDLE_HOLDER_IRON.get(), ModBlocks.CANDLE_HOLDER_GOLD.get(),
                ModBlocks.CANDLE_HOLDER_QUARTZ.get());

        // Wooden blocks — default brown (2)
        var woodenColor = simpleBlockColor(
                s -> NekoColors.getWoodenColorOrBrown(s.getValue(DyeableBlock.COLOR)),
                NekoColors.getWoodenColorOrBrown(2));
        event.register(woodenColor,
                ModBlocks.EASEL_MENU.get(), ModBlocks.EASEL_MENU_WHITE.get(),
                ModBlocks.CUPBOARD.get(), ModBlocks.SHELF.get(), ModBlocks.WALL_SHELF.get(),
                ModBlocks.GLASS_TABLE.get(), ModBlocks.GLASS_ROUND_TABLE.get(),
                ModBlocks.ARM_CHAIR.get(), ModBlocks.BENCH.get(),
                ModBlocks.DRAWER.get(), ModBlocks.CABINET.get(), ModBlocks.DRAWER_CHEST.get());

        // Window frames — default light gray (7)
        var windowFrameColor = simpleBlockColor(
                s -> NekoColors.getStoneColorOrLightGray(s.getValue(DyeableHorizontalBlock.COLOR)),
                NekoColors.getStoneColorOrLightGray(7));
        event.register(windowFrameColor,
                ModBlocks.WINDOW_SILL.get(), ModBlocks.WINDOW_TOP.get(), ModBlocks.WINDOW_FRAME.get());

        // Half-timber — dual color
        var halfTimberColor = tintedBlockColor(
                s -> NekoColors.getWoodenColorOrBrown(s.getValue(HalfTimberBlock.COLOR0)),
                s -> NekoColors.getNekoColorOrWhite(s.getValue(HalfTimberBlock.COLOR1)),
                NekoColors.getWoodenColorOrBrown(2), NekoColors.getNekoColorOrWhite(14));
        event.register(halfTimberColor,
                ModBlocks.HALF_TIMBER_P0.get(), ModBlocks.HALF_TIMBER_P1.get(),
                ModBlocks.HALF_TIMBER_P2.get(), ModBlocks.HALF_TIMBER_P3.get(),
                ModBlocks.HALF_TIMBER_P4.get(), ModBlocks.HALF_TIMBER_P5.get(),
                ModBlocks.HALF_TIMBER_P6.get(), ModBlocks.HALF_TIMBER_P7.get(),
                ModBlocks.HALF_TIMBER_P8.get(), ModBlocks.HALF_TIMBER_P9.get(),
                ModBlocks.HALF_TIMBER_PILLAR_P0.get(), ModBlocks.HALF_TIMBER_PILLAR_P1.get(),
                ModBlocks.HALF_TIMBER_PILLAR_P2.get());

        // Window plant — foliage + color
        event.register((state, view, pos, tintIndex) -> {
            if (view == null || pos == null)
                return tintIndex == 0 ? NekoColors.EnumNekoColor.PURPLE.getColor() : NekoColors.getNekoColorOrWhite(14);
            return tintIndex == 0
                    ? BiomeColors.getAverageFoliageColor(view, pos)
                    : NekoColors.getNekoColorOrWhite(state.getValue(DyeableBlock.COLOR));
        }, ModBlocks.WINDOW_PLANT.get());

        // Windows — default brown (2)
        var windowColor = simpleBlockColor(
                s -> NekoColors.getWoodenColorOrBrown(s.getValue(WindowBlock.COLOR)),
                NekoColors.getWoodenColorOrBrown(2));
        event.register(windowColor,
                ModBlocks.WINDOW_SIMPLE.get(), ModBlocks.WINDOW_ARCH.get(), ModBlocks.WINDOW_CROSS.get(),
                ModBlocks.WINDOW_SHADE.get(), ModBlocks.WINDOW_LANCET.get());

        // Doors — default white (14)
        var doorColor = simpleBlockColor(
                s -> NekoColors.getNekoColorOrWhite(s.getValue(DyeableDoorBlock.COLOR)),
                NekoColors.getNekoColorOrWhite(14));
        event.register(doorColor,
                ModBlocks.DOOR_1.get(), ModBlocks.DOOR_2.get(), ModBlocks.DOOR_3.get(),
                ModBlocks.DOOR_TALL_1.get(), ModBlocks.DOOR_TALL_2.get(), ModBlocks.DOOR_TALL_3.get());

        LOGGER.info("Block Colors Registered.");
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        // Stone items
        ItemColor stoneItemColor = (stack, tintIndex) ->
                NekoColors.getStoneFromNeko(DyeableBlockItem.getColor(stack)).getColor();
        event.register(stoneItemColor,
                ModBlocks.STONE_BASE_BOTTOM.get().asItem(), ModBlocks.STONE_FRAME_BOTTOM.get().asItem(),
                ModBlocks.STONE_PILLAR_BOTTOM.get().asItem(), ModBlocks.STONE_BASE.get().asItem(),
                ModBlocks.STONE_FRAME.get().asItem(), ModBlocks.STONE_PILLAR.get().asItem(),
                ModBlocks.STONE_DORIC.get().asItem(), ModBlocks.STONE_IONIC.get().asItem(),
                ModBlocks.STONE_CORINTHIAN.get().asItem(), ModBlocks.WINDOW_SILL.get().asItem(),
                ModBlocks.WINDOW_TOP.get().asItem(), ModBlocks.WINDOW_FRAME.get().asItem(),
                ModBlocks.STONE_LAYERED.get().asItem(), ModBlocks.STONE_POT.get().asItem(),
                ModBlocks.STONE_PLANTER.get().asItem(), ModBlocks.STONE_BOTTOM_THIN.get().asItem(),
                ModBlocks.STONE_PILLAR_THIN.get().asItem(), ModBlocks.STONE_DORIC_THIN.get().asItem(),
                ModBlocks.STONE_IONIC_THIN.get().asItem(), ModBlocks.STONE_CORINTHIAN_THIN.get().asItem());

        // Candle holders
        event.register(stoneItemColor,
                ModBlocks.CANDLE_HOLDER_IRON.get().asItem(), ModBlocks.CANDLE_HOLDER_GOLD.get().asItem(),
                ModBlocks.CANDLE_HOLDER_QUARTZ.get().asItem());

        // Window plant
        event.register((stack, tintIndex) -> tintIndex == 0
                        ? FoliageColor.getDefaultColor()
                        : DyeableBlockItem.getColor(stack).getColor(),
                ModBlocks.WINDOW_PLANT.get().asItem());

        // Wooden items
        ItemColor woodenItemColor = (stack, tintIndex) ->
                DyeableWoodenBlockItem.getColor(stack).getColor();
        event.register(woodenItemColor,
                ModBlocks.WINDOW_SIMPLE.get().asItem(), ModBlocks.WINDOW_ARCH.get().asItem(),
                ModBlocks.WINDOW_CROSS.get().asItem(), ModBlocks.WINDOW_SHADE.get().asItem(),
                ModBlocks.WINDOW_LANCET.get().asItem(), ModBlocks.EASEL_MENU.get().asItem(),
                ModBlocks.EASEL_MENU_WHITE.get().asItem(), ModBlocks.GLASS_TABLE.get().asItem(),
                ModBlocks.GLASS_ROUND_TABLE.get().asItem(), ModBlocks.CUPBOARD.get().asItem(),
                ModBlocks.SHELF.get().asItem(), ModBlocks.WALL_SHELF.get().asItem(),
                ModBlocks.BENCH.get(), ModBlocks.ARM_CHAIR.get().asItem(),
                ModBlocks.DRAWER.get().asItem(), ModBlocks.CABINET.get().asItem(),
                ModBlocks.DRAWER_CHEST.get().asItem());

        // Half-timber — dual color
        ItemColor halfTimberItemColor = (stack, tintIndex) -> tintIndex == 0
                ? HalfTimberBlockItem.getColor0(stack).getColor()
                : HalfTimberBlockItem.getColor1(stack).getColor();
        event.register(halfTimberItemColor,
                ModBlocks.HALF_TIMBER_P0.get().asItem(), ModBlocks.HALF_TIMBER_P1.get().asItem(),
                ModBlocks.HALF_TIMBER_P2.get().asItem(), ModBlocks.HALF_TIMBER_P3.get().asItem(),
                ModBlocks.HALF_TIMBER_P4.get().asItem(), ModBlocks.HALF_TIMBER_P5.get().asItem(),
                ModBlocks.HALF_TIMBER_P6.get().asItem(), ModBlocks.HALF_TIMBER_P7.get().asItem(),
                ModBlocks.HALF_TIMBER_P8.get().asItem(), ModBlocks.HALF_TIMBER_P9.get().asItem(),
                ModBlocks.HALF_TIMBER_PILLAR_P0.get().asItem(), ModBlocks.HALF_TIMBER_PILLAR_P1.get().asItem(),
                ModBlocks.HALF_TIMBER_PILLAR_P2.get().asItem());

        LOGGER.info("Item Colors Registered.");
    }

    // --- Layer definitions ---

    public static final ModelLayerLocation WALLPAPER =
            new ModelLayerLocation(new ResourceLocation(Nekoration.MODID, "wallpaper"), "main");

    @SubscribeEvent
    public static void RegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(WALLPAPER, WallPaperRenderer::createBodyLayer);
    }

    // --- Shaders ---

    @Nullable
    private static ShaderInstance rendertypeCatPortalShader;

    @Nullable
    public static ShaderInstance getRendertypeCatPortalShader() {
        return rendertypeCatPortalShader;
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void RegisterShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        Nekoration.MODID + ":rendertype_cat_portal", DefaultVertexFormat.POSITION),
                inst -> rendertypeCatPortalShader = inst);
    }
}