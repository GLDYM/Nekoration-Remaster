package com.flechazo.nekoration.network;

import com.flechazo.nekoration.Nekoration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Nekoration.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        int networkId = 0;
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                C2SUpdateEaselMenuData.class,
                C2SUpdateEaselMenuData::encode,
                C2SUpdateEaselMenuData::decode,
                C2SUpdateEaselMenuData::handle
        );
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                S2CUpdateEaselMenuData.class,
                S2CUpdateEaselMenuData::encode,
                S2CUpdateEaselMenuData::decode,
                S2CUpdateEaselMenuData::handle
        );
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                C2SUpdatePaletteData.class,
                C2SUpdatePaletteData::encode,
                C2SUpdatePaletteData::decode,
                C2SUpdatePaletteData::handle
        );
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                C2SUpdatePaintingData.class,
                C2SUpdatePaintingData::encode,
                C2SUpdatePaintingData::decode,
                C2SUpdatePaintingData::handle
        );
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                C2SUpdatePaintingSize.class,
                C2SUpdatePaintingSize::encode,
                C2SUpdatePaintingSize::decode,
                C2SUpdatePaintingSize::handle
        );
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                S2CUpdatePaintingData.class,
                S2CUpdatePaintingData::encode,
                S2CUpdatePaintingData::decode,
                S2CUpdatePaintingData::handle
        );
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                S2CUpdateWallpaperPart.class,
                S2CUpdateWallpaperPart::encode,
                S2CUpdateWallpaperPart::decode,
                S2CUpdateWallpaperPart::handle
        );
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                S2CUpdateCupboardData.class,
                S2CUpdateCupboardData::encode,
                S2CUpdateCupboardData::decode,
                S2CUpdateCupboardData::handle
        );
        ModPacketHandler.CHANNEL.registerMessage(networkId++,
                S2CUpdateCustomBlockData.class,
                S2CUpdateCustomBlockData::encode,
                S2CUpdateCustomBlockData::decode,
                S2CUpdateCustomBlockData::handle
        );
    }
}