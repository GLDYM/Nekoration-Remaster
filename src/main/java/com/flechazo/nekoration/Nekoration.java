package com.flechazo.nekoration;

import com.flechazo.nekoration.blocks.ModBlocks;
import com.flechazo.nekoration.blocks.containers.ModMenuType;
import com.flechazo.nekoration.blocks.entities.ModBlockEntityType;
import com.flechazo.nekoration.common.event.CommonModEventSubscriber;
import com.flechazo.nekoration.entities.ModEntityType;
import com.flechazo.nekoration.items.ModItems;
import com.flechazo.nekoration.network.ModPacketHandler;
import com.flechazo.nekoration.recipes.ModRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Nekoration.MODID)
public class Nekoration {
    public static final String MODID = "nekoration";

    public static final Logger LOGGER = LogManager.getLogger();

    public Nekoration() {
        LOGGER.info("Meow~~ Miaow~~~");
        MinecraftForge.EVENT_BUS.register(this);

        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, NekoConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, NekoConfig.SERVER_SPEC);
        ModPacketHandler.register();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntityType.ENTITY_TYPES.register(modEventBus);
        ModMenuType.MENU_TYPES.register(modEventBus);
        ModBlockEntityType.TILE_ENTITY_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        CommonModEventSubscriber.CREATIVE_MODE_TABS.register(modEventBus);
    }
}
