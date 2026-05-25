package com.flechazo.nekoration.client;

import com.flechazo.nekoration.client.gui.screen.PaintingScreen;
import com.flechazo.nekoration.client.gui.screen.PaintingSizeScreen;
import com.flechazo.nekoration.client.gui.screen.PaletteScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

import java.awt.*;

public class ClientHelper {
    public static void showPaletteScreen(InteractionHand hand, byte active, Color[] colors) {
        Minecraft.getInstance().setScreen(new PaletteScreen(hand, active, colors));
    }

    public static void showPaintingSizeScreen(InteractionHand hand, int count) {
        Minecraft.getInstance().setScreen(new PaintingSizeScreen(hand, count));
    }

    public static void showPaintingScreen(int painting) {
        Minecraft.getInstance().setScreen(new PaintingScreen(painting));
    }

    public static void showPaintingScreen(int painting, byte active, Color[] colors) {
        Minecraft.getInstance().setScreen(new PaintingScreen(painting, active, colors));
    }
}
