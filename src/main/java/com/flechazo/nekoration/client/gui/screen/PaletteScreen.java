package com.flechazo.nekoration.client.gui.screen;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.Nekoration;
import com.flechazo.nekoration.network.C2SUpdatePaletteData;
import com.flechazo.nekoration.network.ModPacketHandler;
import com.flechazo.nekoration.utils.VoxelShapeHighlighter;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Objects;

public class PaletteScreen extends Screen {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Nekoration.MODID, "textures/gui/palette.png");

    public static final int COLORMAP_LEFT = 9;
    public static final int COLORMAP_TOP = 32;
    public static final int COLORMAP_WIDTH = 128;
    public static final int COLORMAP_HEIGHT = 128;

    public static final int HUE_LEFT = 141;
    public static final int HUE_TOP = 32;
    public static final int HUE_WIDTH = 6;
    public static final int HUE_HEIGHT = 128;
    public static final int white = (255 << 24) + (255 << 16) + (255 << 8) + 255; // a, r, g, b...
    public static final int black = 255 << 24; // a, r, g, b...

    private final int imageWidth = 156;
    private final int imageHeight = 166;

    private int leftPos;
    private int topPos;

    private Color colorMapColor = Color.RED;
    private Color[] colors = new Color[6];
    private byte activeSlot = 0;

    private int huePos = -1;
    private final int[] colorPos = {-1, -1};
    private final InteractionHand hand;

    public boolean renderColorText = false;

    private final Component tipMessage;

    public PaletteScreen(InteractionHand hand, byte active, Color[] oldColors) {
        super(Component.nullToEmpty("PALETTE"));
        this.hand = hand;
        this.colors = oldColors;
        tipMessage = Component.translatable("gui.nekoration.message.press_key_color_info", "'E'");
        setActiveSlot(active);
    }

    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void onClose() {
        VoxelShapeHighlighter.PaletteColor = colors[activeSlot];
        int[] cls = new int[6];
        for (int idx = 0; idx < 6; idx++)
            cls[idx] = colors[idx].getRGB();
        ModPacketHandler.CHANNEL.sendToServer(new C2SUpdatePaletteData(this.hand, activeSlot, cls));
        super.onClose();
    }

    @Override
    @SuppressWarnings({"resource"})
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        return switch (keyCode) {
            case GLFW.GLFW_KEY_E -> {
                this.renderColorText = !this.renderColorText;
                yield true;
            }
            case GLFW.GLFW_KEY_ESCAPE -> {
                Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player).closeContainer();
                yield true;
            }
            default -> super.keyPressed(keyCode, scanCode, modifier);
        };
    }

    public void render(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        int i = this.leftPos;
        int j = this.topPos;

        // Step 0: 填充背景
        renderBackground(guiGraphics);

        // Step 1: 渲染6个颜色槽和中间的"选中颜色"槽
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        for (int idx = 0; idx < 6; idx++) {
            RenderSystem.setShaderColor(colors[idx].getRed() / 255.0F, colors[idx].getGreen() / 255.0F, colors[idx].getBlue() / 255.0F, 1.0F);
            guiGraphics.blit(BACKGROUND, i + 8 + 18 * idx + (idx > 2 ? 34 : 0), j + 13, 172, 32, 16, 16); // 纯白色方块着色
            if (idx == activeSlot) {
                guiGraphics.blit(BACKGROUND, i + 70, j + 13, 172, 32, 16, 16);
            }
        }

        // Step 2: 渲染背景
        renderBg(guiGraphics, partialTicks, x, y);

        // Step 3: 渲染活动槽指示器
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(BACKGROUND, i + 8 + 18 * activeSlot + (activeSlot > 2 ? 34 : 0), j + 13, 172, 16, 16, 16); // 槽指示器

        // Step 4: 渲染颜色图
        var pose = guiGraphics.pose();
        pose.pushPose();
        pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
        pose.translate(j + 32, -i - 137, 0);
        guiGraphics.fillGradient(0, 0, 128, 128, colorMapColor.getRGB(), white);
        pose.popPose();
        guiGraphics.fillGradient(i + COLORMAP_LEFT, j + COLORMAP_TOP, i + COLORMAP_LEFT + 128, j + COLORMAP_TOP + 128, 0, black);

        // Step 5: 渲染色调和颜色光标
        if (huePos >= 0)
            guiGraphics.blit(BACKGROUND, i + HUE_LEFT - 1, huePos + this.topPos - 1, 156, 48, 8, 4); // 色调光标
        if (colorPos[0] >= 0)
            guiGraphics.blit(BACKGROUND, this.leftPos + colorPos[0] - 2, this.topPos + colorPos[1] - 2, 172, 48, 4, 4); // 颜色光标

        // Step 6: 渲染调试颜色值
        pose = guiGraphics.pose();
        pose.pushPose();
        pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
        pose.translate(j, -i - 167, 0);
        if (renderColorText)
            guiGraphics.drawString(this.font, Component.translatable("gui.nekoration.message.color_info", colors[activeSlot].getRGB(), colors[activeSlot].getRed(), colors[activeSlot].getGreen(), colors[activeSlot].getBlue()), 1, 1, colors[activeSlot].getRGB(), false);
        else
            guiGraphics.drawString(this.font, tipMessage, 1, 1, (150 << 24) + (255 << 16) + (255 << 8) + 255, false);
        pose.popPose();
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int edgeSpacingX = (this.width - this.imageWidth) / 2;
        int edgeSpacingY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(BACKGROUND, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        super.renderBackground(guiGraphics);
        guiGraphics.fillGradient(0, 0, width, height, -1072689136, -804253680);
    }


    @Override
    public boolean mouseClicked(double x, double y, int type) {
        if (type == 0 && !updateActiveSlot(x, y)) { // Left Mouse Only, and first update slots...
            if (isOnColorMap(x, y))
                getColor(x, y);
            if (isOnHuePicker(x, y))
                getHue(x, y);
        }
        return super.mouseClicked(x, y, type);
    }

    @Override
    public boolean mouseDragged(double x, double y, int type, double dx, double dy) {
        if (type == 0) {
            if (isOnColorMap(x, y))
                getColor(x, y);
            if (isOnHuePicker(x, y))
                getHue(x, y);
        }
        return super.mouseDragged(x, y, type, dx, dy);
    }

    private boolean isOnColorMap(double x, double y) {
        double dx = x - this.leftPos - COLORMAP_LEFT;
        double dy = y - this.topPos - COLORMAP_TOP;
        return dx >= 0.0D && dy >= 0.0D && dx <= COLORMAP_WIDTH && dy <= COLORMAP_HEIGHT;
    }

    private boolean updateActiveSlot(double x, double y) {
        for (int idx = 0; idx < 6; idx++) {
            int l = this.leftPos + 8 + 18 * idx + (idx > 2 ? 34 : 0);
            int r = l + 16;
            int t = this.topPos + 13;
            int b = t + 16;
            if (x >= l && x <= r && y >= t && y <= b && this.activeSlot != idx) {
                setActiveSlot(idx);
                return true;
            }
        }
        return false;
    }

    private void setActiveSlot(int slot) {
        this.activeSlot = (byte) slot;
        // And also update that hue picker & color map...
        Color nw = colors[slot];
        float[] fl = Color.RGBtoHSB(nw.getRed(), nw.getGreen(), nw.getBlue(), null); // Hue, Saturation, Value(or to say Brightness)...
        this.huePos = HUE_TOP + (int) ((1.0F - fl[0]) * HUE_HEIGHT);
        this.colorMapColor = Color.getHSBColor(fl[0], 1.0F, 1.0F);
    }

    private void getColor(double x, double y) {
        double xi = (x - this.leftPos - COLORMAP_LEFT) / COLORMAP_WIDTH;
        double yi = (y - this.topPos - COLORMAP_TOP) / COLORMAP_HEIGHT;
        if (applyColor(xi, yi)) {
            colorPos[0] = (int) x - leftPos;
            colorPos[1] = (int) y - topPos;
        }
    }

    private void updateColor() {
        double xi = (double) (colorPos[0] - COLORMAP_LEFT) / COLORMAP_WIDTH;
        double yi = (double) (colorPos[1] - COLORMAP_TOP) / COLORMAP_HEIGHT;
        applyColor(xi, yi);
    }

    private boolean applyColor(double xi, double yi) {
        if (xi < 0.0D || xi > 1.0D || yi < 0.0D || yi > 1.0D) return false;
        if (activeSlot < 0 || activeSlot >= colors.length) return false;
        Color c1 = NekoColors.getRGBColorBetween(xi, Color.WHITE, colorMapColor);
        colors[activeSlot] = NekoColors.getRGBColorBetween(yi, c1, Color.BLACK);
        return true;
    }

    private boolean isOnHuePicker(double x, double y) {
        double dx = x - this.leftPos - HUE_LEFT;
        double dy = y - this.topPos - HUE_TOP;
        return dx >= 0.0D && dy >= 0.0D && dx <= HUE_WIDTH && dy <= HUE_HEIGHT;
    }

    private void getHue(double x, double y) {
        double yi = 1.0D - (y - this.topPos - COLORMAP_TOP) / COLORMAP_WIDTH;
        this.colorMapColor = Color.getHSBColor((float) yi, 1.0F, 1.0F);
        // Update Active Color...
        updateColor();
        this.huePos = (int) y - this.topPos;
    }

    @Override
    public boolean isPauseScreen() {
        return false; // returns ture by default... interesting...
    }
}