package com.flechazo.nekoration.client.event;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PhotoEvents {
    private static final Logger LOGGER = LogManager.getLogger("Photo");

    private static Minecraft mc;
    public static final AtomicBoolean shouldTakePhoto = new AtomicBoolean(false);
    private static long lastShot = 0L;

    @SubscribeEvent
    public static void onRenderWorldFinish(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER || !shouldTakePhoto.get()) return;

        long now = System.nanoTime();
        if (now - lastShot < 500000000L) return;
        lastShot = now;

        shouldTakePhoto.set(false);

        if (mc == null) mc = Minecraft.getInstance();

        NativeImage nativeimage = Screenshot.takeScreenshot(mc.getMainRenderTarget());

        Util.ioPool().execute(() -> {
            try {
                int i = nativeimage.getWidth();
                int j = nativeimage.getHeight();
                int k = 0;
                int l = 0;
                if (i > j) {
                    k = (i - j) / 2;
                    i = j;
                } else {
                    l = (j - i) / 2;
                    j = i;
                }

                File targetDir = new File(mc.gameDirectory, "nekopaint/photo/");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }
                try (NativeImage photo = new NativeImage(96, 96, false)) {
                    nativeimage.resizeSubRectTo(k, l, i, j, photo);

                    String fileName = UUID.randomUUID() + ".png";
                    photo.writeToFile(new File(targetDir, fileName));
                }
            } catch (IOException ioexception) {
                LOGGER.error("Failed to save photo", ioexception);
            } finally {
                nativeimage.close();
            }
        });
    }
}