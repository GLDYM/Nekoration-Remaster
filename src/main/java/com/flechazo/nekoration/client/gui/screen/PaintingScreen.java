package com.flechazo.nekoration.client.gui.screen;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.NekoConfig;
import com.flechazo.nekoration.Nekoration;
import com.flechazo.nekoration.client.gui.widget.IconButton;
import com.flechazo.nekoration.entities.PaintingData;
import com.flechazo.nekoration.entities.PaintingEntity;
import com.flechazo.nekoration.items.PaletteItem;
import com.flechazo.nekoration.network.C2SUpdatePaintingData;
import com.flechazo.nekoration.network.ModPacketHandler;
import com.flechazo.nekoration.utils.URLHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class PaintingScreen extends Screen {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Nekoration.MODID, "textures/gui/painting.png");
    public static final ResourceLocation ICONS = new ResourceLocation(Nekoration.MODID, "textures/gui/icons.png");

    public static final int PAINTING_LEFT = 9;
    public static final int PAINTING_TOP = 38;
    public static final int PAINTING_WIDTH = 225;
    public static final int PAINTING_HEIGHT = 145;

    public static final int OPACITY_LEFT = 241;
    public static final int OPACITY_TOP = 38;
    public static final int OPACITY_WIDTH = 6;
    public static final int OPACITY_HEIGHT = 145;

    public static final int TOOLS_LEFT = 148;
    public static final int TOOLS_TOP = 13;
    public static final int TOOLS_NUM = 4;

    public static final int white = (255 << 24) + (255 << 16) + (255 << 8) + 255; // a, r, g, b...
    public static final int black = 255 << 24; // a, r, g, b...

    private final int imageWidth = 256;
    private final int imageHeight = 192;

    private final Deque<int[]> history = new LinkedList<>();
    private final Deque<int[]> future = new LinkedList<>();

    private int leftPos;
    private int topPos;
    private final int entityId;
    private final Color[] colors;
    private byte activeSlot;
    private int opacity = 255;
    private int opacityPos;
    private byte activeTool = 0; // 0: Pencil, 1: Brush, 2: Eraser, 3: Bucket Fill
    private static final int[] toolParams = {1, 1, 2, 5};
    private static final int[] MAX_PARAMS = {10, 10, 16, 99};
    public boolean renderDebugText = false;

    private final PaintingData paintingData;
    private final short paintingWidth;
    private final short paintingHeight;

    private final int oldHash;

    private EditBox nameInput;
    private boolean nameError = false;
    private final IconButton[] buttons = new IconButton[5];

    // Used on Client-Side only
    private static double hor = 0.0D, ver = 0.0D;
    private static int pixsize = 8;
    private static int lastEdited = 0;

    private static int renderTime = 0;
    private static final int TIPS = 3;
    private final Component[] tipMessages = new Component[TIPS];
    private static final String[] buttonKeys = {"save_painting", "save_painting_content", "load_image", "clear", "round_brush", "square_brush", "transp_add_up", "transp_overwrite"};
    private final Component[] buttonMessages = new Component[buttonKeys.length];
    private final Component[] paramMessages = new Component[TOOLS_NUM];
    private static int stepLimit;

    // Config
    private static boolean roundBrush = true;
    private static boolean transBlend = true;

    public PaintingScreen(int pt) {
        this(pt, (byte) 0, PaletteItem.DEFAULT_COLOR_SET);
    }

    @SuppressWarnings({"resource", "null"})
    public PaintingScreen(int pt, byte active, Color[] paletteColors) {
        super(Component.nullToEmpty("PAINTING"));
        opacityPos = topPos + OPACITY_TOP;
        activeSlot = active;
        colors = paletteColors;
        entityId = pt;
        PaintingEntity painting = (PaintingEntity) Minecraft.getInstance().level.getEntity(entityId);
        paintingData = painting.data;
        paintingWidth = painting.data.getWidth();
        paintingHeight = painting.data.getHeight();
        paintingData.imageReady = false;
        oldHash = paintingData.getPaintingHash();
        if (oldHash != lastEdited) {
            // Reset editor window position and scale...
            hor = ver = 0.0D;
            pixsize = 8;
        } // Or if we're editing the painting this client last edited, just keep the editor's transforms.
        tipMessages[0] = Component.translatable("gui.nekoration.message.press_key_debug_info", "'F1'");
        tipMessages[1] = Component.translatable("gui.nekoration.message.press_key_undo_redo", "'Z'/'X'");
        tipMessages[2] = Component.translatable("gui.nekoration.message.press_key_change_tool", "'W'");
        for (int idx = 0; idx < buttonKeys.length; idx++)
            buttonMessages[idx] = Component.translatable("gui.nekoration.button." + buttonKeys[idx]);
        for (int idx = 0; idx < TOOLS_NUM; idx++)
            paramMessages[idx] = Component.translatable("gui.nekoration.message.scroll_change", Component.translatable("gui.nekoration.paint.tool_param" + idx).getString());
        history.clear();
        future.clear();
        stepLimit = NekoConfig.CLIENT.maxUndoLimit.get();
    }

    private String[] getFileLocation() {
        String file = nameInput.getValue().trim();
        if (file.equals(""))
            return new String[]{"nekopaint", String.valueOf(paintingData.getPaintingHash())};
        int slashIdx = Math.max(file.lastIndexOf("/"), file.lastIndexOf("\\"));
        if (slashIdx == -1)
            return new String[]{"nekopaint", file};
        else return new String[]{"nekopaint/" + file.substring(0, slashIdx + 1), file.substring(slashIdx + 1)};
    }

    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.nameInput = new EditBox(this.font, leftPos + 57, topPos - 18, 121, 16, Component.translatable("gui.nekoration.color"));
        this.nameInput.setMaxLength(256);
        this.nameInput.setResponder(input -> {
            if (nameError) {
                this.nameInput.setTextColor(0xFFFFFF); // No more Red Error text if text changed.
                nameError = false;
                // Get the Load Button back...
                buttons[2].setIcon(ICONS, 32, 16);
                buttons[2].setMessage(buttonMessages[2]);
            }
        });
        buttons[0] = new IconButton(leftPos + 180, topPos - 20, buttonMessages[0], button -> {
            // Save Image...
            try {
                String[] location = getFileLocation();
                paintingData.save(location[0], location[1], true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ICONS, 0, 16);
        buttons[1] = new IconButton(leftPos + 200, topPos - 20, buttonMessages[1], button -> {
            // Save Image Content...
            try {
                String[] location = getFileLocation();
                paintingData.save(location[0], location[1], false, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ICONS, 16, 16);
        buttons[2] = new IconButton(leftPos + 220, topPos - 20, buttonMessages[2], button -> {
            if (nameError) { // It's a 'Clear' Button
                nameInput.setValue("");
                nameInput.setTextColor(0xFFFFFF);
                nameError = false;
                // Get the Load Button back...
                buttons[2].setIcon(ICONS, 32, 16);
                buttons[2].setMessage(buttonMessages[2]);
                return;
            }
            // Load Image File...
            if (nameInput.getValue().trim().equals("")) {
                nameInput.setValue("Input the name here...");
                nameError = true;
            } else {
                try {
                    if (URLHelper.isURL(nameInput.getValue().split(">")[0].trim())) {
                        nameError = !paintingData.load("<url>", nameInput.getValue().trim());
                    } else {
                        String[] location = getFileLocation();
                        nameError = !paintingData.load(location[0], location[1]);
                    }
                } catch (MalformedURLException | URISyntaxException e) {
                    nameError = true;
                }
            }
            if (nameError) {
                // Make the text Red...
                nameInput.setTextColor(0xFF0000);
                // Turn the 3rd button into a clear button...
                buttons[2].setIcon(ICONS, 32, 0);
                buttons[2].setMessage(buttonMessages[3]);
            }
        }, ICONS, 32, 16);
        // Config Buttons...
        buttons[3] = new IconButton(leftPos + 15, topPos - 20, roundBrush ? buttonMessages[4] : buttonMessages[5], button -> {
            roundBrush = !roundBrush;
            buttons[3].setIcon(ICONS, roundBrush ? 48 : 64, 16);
            buttons[3].setMessage(roundBrush ? buttonMessages[4] : buttonMessages[5]);
        }, ICONS, roundBrush ? 48 : 64, 16);
        buttons[4] = new IconButton(leftPos + 35, topPos - 20, transBlend ? buttonMessages[6] : buttonMessages[7], button -> {
            transBlend = !transBlend;
            buttons[4].setIcon(ICONS, transBlend ? 80 : 96, 16);
            buttons[4].setMessage(transBlend ? buttonMessages[6] : buttonMessages[7]);
        }, ICONS, transBlend ? 80 : 96, 16);
        this.addWidget(nameInput);
        for (int btn = 0; btn < 5; btn++)
            this.addWidget(buttons[btn]);
        renderTime = 40;
    }

    @Override
    public void tick() {
        nameInput.tick();
    }

    @Override
    public void onClose() {
        if (oldHash != paintingData.getPaintingHash()) {
            paintingData.clearCache(oldHash);
            byte blocW = (byte) (paintingWidth / 16);
            byte blocH = (byte) (paintingHeight / 16);
            for (byte blocX = 0; blocX < blocW; blocX += 3) {
                for (byte blocY = 0; blocY < blocH; blocY += 3) {
                    byte ptW = (byte) Math.min(blocW - blocX, 3);
                    byte ptH = (byte) Math.min(blocH - blocY, 3);
                    final int[] partPixels = new int[ptW * 16 * ptH * 16];
                    for (int i = 0; i < ptW * 16; i++)
                        for (int j = 0; j < ptH * 16; j++)
                            partPixels[j * ptW * 16 + i] = paintingData.getPixelAt(blocX * 16 + i, blocY * 16 + j);
                    ModPacketHandler.CHANNEL.sendToServer(new C2SUpdatePaintingData(
                            entityId, (byte) (blocX / 3), (byte) (blocY / 3), ptW, ptH,
                            partPixels, paintingData.getPaintingHash()));
                }
            }
            if (NekoConfig.CLIENT.useImageRendering.get())
                paintingData.cache();
        } else {
            paintingData.imageReady = true;
        }
        lastEdited = paintingData.getPaintingHash();
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        if (keyCode == GLFW.GLFW_KEY_F1) {
            this.renderDebugText = !this.renderDebugText;
            return true;
        } else if (this.nameInput.isFocused()) {
            return super.keyPressed(keyCode, scanCode, modifier);
        } else if (keyCode == GLFW.GLFW_KEY_W) {
            // Switch Tool...
            activeTool = (byte) ((activeTool + 1) % TOOLS_NUM);
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_Z) {
            // Undo...
            int[] futureCopy = Arrays.copyOf(paintingData.getPixels(), paintingData.getPixels().length);
            int[] target = history.pollFirst(); // Pop
            if (target != null) { // Previous step available...
                paintingData.setPixels(target);
                future.offerFirst(futureCopy); // Push
                if (future.size() > stepLimit)
                    future.pollLast();
            }
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_X) {
            // Redo...
            int[] pastCopy = Arrays.copyOf(paintingData.getPixels(), paintingData.getPixels().length);
            int[] target = future.pollFirst(); // Pop
            if (target != null) { // Future step available...
                paintingData.setPixels(target);
                history.offerFirst(pastCopy); // Push
                if (history.size() > stepLimit)
                    history.pollLast();
            }
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player).closeContainer();
        }
        return super.keyPressed(keyCode, scanCode, modifier);
    }

    private String debugText = "Ceci n'est pas une ligne de texte.";


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int i = this.leftPos;
        int j = this.topPos;
        PoseStack stack = graphics.pose();

        // Step 0: Fill the back ground...
        graphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        // Step 1: Render the 6 color slots, and the 'selected color' slot in the middle...
        for (int idx = 0; idx < 6; idx++) {
            graphics.setColor(colors[idx].getRed() / 255.0F, colors[idx].getGreen() / 255.0F, colors[idx].getBlue() / 255.0F, 1.0F);
            graphics.blit(BACKGROUND, i + 34 + 18 * idx, j + 13, 16, 224, 16, 16);
            if (idx == activeSlot) {
                graphics.blit(BACKGROUND, i + 8, j + 13, 16, 224, 16, 16);
            }
        }
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Step 2: Render the painting...
        for (short posi = 0; posi < paintingWidth; posi++) {
            if (hor + (posi + 1) * pixsize < 0 || hor + posi * pixsize > PAINTING_WIDTH)
                continue;
            for (short posj = 0; posj < paintingHeight; posj++) {
                if (ver + (posj + 1) * pixsize < 0 || ver + posj * pixsize > PAINTING_HEIGHT)
                    continue;
                Color color = NekoColors.getRGBColor(paintingData.getCompositeAt(posi, posj));
                graphics.setColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
                graphics.blit(BACKGROUND, i + PAINTING_LEFT + (int) hor + posi * pixsize, j + PAINTING_TOP + (int) ver + posj * pixsize, 16, 224, pixsize, pixsize);
            }
        }
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Step 3: Render the back ground...
        this.renderBg(graphics, partialTicks, mouseX, mouseY);

        // Step 4: Render Active Slot Indicator...
        graphics.blit(BACKGROUND, i + 34 + 18 * activeSlot, j + 13, 16, 208, 16, 16);

        // Step 5: Render Opacity Bar...
        graphics.fillGradient(i + OPACITY_LEFT, j + OPACITY_TOP, i + OPACITY_LEFT + OPACITY_WIDTH, j + OPACITY_TOP + OPACITY_HEIGHT, colors[activeSlot].getRGB(), colors[activeSlot].getRGB() & 0xffffff); // 修改

        // Step 6: Render Opacity cursor...
        if (opacityPos >= 0)
            graphics.blit(BACKGROUND, i + OPACITY_LEFT - 1, opacityPos + this.topPos - 1, 0, 240, 8, 4);
        // Step 7: Render Active Tool Icon...
        graphics.blit(BACKGROUND, i + TOOLS_LEFT + activeTool * 17, j + TOOLS_TOP, 32 + activeTool * 16, 208, 16, 16);

        // Step 8: Render Import/Export Controls...
        nameInput.render(graphics, mouseX, mouseY, partialTicks);
        for (int btn = 0; btn < 5; btn++) {
            buttons[btn].render(graphics, mouseX, mouseY, partialTicks);
        }
        // Tooltips are rendered after main rendering loop in Screen class usually, but if done here:
        super.render(graphics, mouseX, mouseY, partialTicks); // Render widgets added via addRenderableWidget
        for (int tip = 0; tip < 5; tip++) {
            if (buttons[tip].isMouseOver(mouseX, mouseY)) {
                graphics.renderTooltip(this.font, buttons[tip].getMessage(), mouseX, mouseY);
            }
        }

        // Step 9: Render Debug Text...
        stack.pushPose();
        stack.translate(i + 221, j + 17, 0);
        graphics.drawString(this.font, "<" + String.format("%02d", toolParams[activeTool]) + ">", 1, 1, 0xFFFFFF);
        stack.popPose();

        stack.pushPose();
        stack.translate(i + 221, j + 17, 0); // 再次应用相同的平移，因为之前的 popPose 恢复了
        stack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        stack.translate(-12, -44, 0);
        if (isOnToolParam(mouseX, mouseY)) {
            graphics.drawString(this.font, paramMessages[activeTool], 1, 1, 0xFFFFFF);
        } else {
            if (renderDebugText)
                graphics.drawString(this.font, debugText, 1, 1, 0xFFFFFF);
            else {
                renderTime++;
                renderTime %= 300 * TIPS;
                int ap = renderTime % 300;
                if (ap == 0)
                    ap = 5;
                else if (ap < 20)
                    ap *= 8;
                else if (ap > 280)
                    ap = (300 - ap) * 8;
                else ap = 160;

                graphics.drawString(this.font, tipMessages[renderTime / 300], 1, 1, (ap << 24) + (255 << 16) + (255 << 8) + 255);
            }
        }
        stack.popPose();
    }

    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight); // 修改
        // 确保在绘制其他东西之前重置颜色（如果需要）
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }


    @Override
    public boolean mouseClicked(double x, double y, int type) {
        if (type == 0 && !updateActiveSlot(x, y)) { // Left Mouse Only, and first update slots...
            if (isOnOpacityPicker(x, y)) {
                getOpacity(x, y);
                debugText = "Opacity: " + opacity;
            } else if (isOnPainting(x, y)) {
                history.offerFirst(Arrays.copyOf(paintingData.getPixels(), paintingData.getPixels().length));
                if (history.size() > stepLimit)
                    history.pollLast();
                future.clear();
                useTool(x, y);
            } else {
                for (byte idx = 0; idx < TOOLS_NUM; idx++) {
                    if (isOn(x - TOOLS_LEFT - idx * 17, y - TOOLS_TOP, 16, 16))
                        activeTool = idx;
                }
            }
        }
        return super.mouseClicked(x, y, type);
    }

    private boolean isOn(double x, double y, double w, double h) {
        double dx = x - this.leftPos;
        double dy = y - this.topPos;
        return dx >= 0.0D && dy >= 0.0D && dx <= w && dy <= h;
    }

    @Override
    public boolean mouseDragged(double x, double y, int type, double dx, double dy) {
        //getColor(x, y);
        if (type == 0) { // Left Button, draw...
            if (isOnPainting(x, y)) {
                useTool(x, y);
            } else if (isOnOpacityPicker(x, y)) {
                getOpacity(x, y);
                debugText = "Opacity: " + opacity;
            }
        } else if (type == 2) { // Middle Button, drag...
            hor += dx;
            ver += dy;
            debugText = "Position: " + x + ", " + y + " -> " + dx + ", " + dy;
        }
        return super.mouseDragged(x, y, type, dx, dy);
    }

    private void useTool(double x, double y) {
        double areax = x - this.leftPos - PAINTING_LEFT;
        double areay = y - this.topPos - PAINTING_TOP;
        double pixX = (areax - hor) / (double) pixsize;
        double pixY = (areay - ver) / (double) pixsize;
        switch (activeTool) {
            case 0: // Pencil
                debugText = String.format("Stroke: [%.2f, %.2f]", pixX, pixY);
                usePencil(pixX, pixY);
                break;
            case 1: // Pen
                debugText = String.format("Draw: [%.2f, %.2f]", pixX, pixY);
                usePen(pixX, pixY);
                break;
            case 2: // Eraser
                debugText = String.format("Erase: [%.2f, %.2f]", pixX, pixY);
                useEraser(pixX, pixY);
                break;
            case 3: // Bucket
                debugText = String.format("Fill: [%.2f, %.2f]", pixX, pixY);
                useBucket(pixX, pixY);
                break;
        }
    }

    private void usePencil(double x, double y) {
        int pixX = (int) x, pixY = (int) y;
        for (int i = -toolParams[0]; i <= toolParams[0]; i++)
            for (int j = -toolParams[0]; j <= toolParams[0]; j++)
                if (!roundBrush || i * i + j * j <= toolParams[0] * toolParams[0])
                    paintingData.setPixel(pixX + i, pixY + j, (opacity << 24) + colors[activeSlot].getRGB(), transBlend);
    }

    private void usePen(double x, double y) {
        int pixX = (int) x, pixY = (int) y;
        for (int i = -toolParams[1]; i <= toolParams[1]; i++)
            for (int j = -toolParams[1]; j <= toolParams[1]; j++)
                if (roundBrush) {
                    int dis2 = i * i + j * j;
                    if (dis2 <= toolParams[1] * toolParams[1]) {
                        float frct = 1.0F - Mth.clamp(Mth.sqrt(dis2) / ((float) toolParams[1] + 0.1F), 0.0F, 1.0F);
                        paintingData.setPixel(pixX + i, pixY + j, (((int) (frct * opacity)) << 24) + colors[activeSlot].getRGB(), transBlend);
                    }
                } else {
                    int dis = Math.max(Math.abs(i), Math.abs(j));
                    float frct = 1.0F - Mth.clamp(dis / ((float) toolParams[1] + 0.1F), 0.0F, 1.0F);
                    paintingData.setPixel(pixX + i, pixY + j, (((int) (frct * opacity)) << 24) + colors[activeSlot].getRGB(), transBlend);
                }
    }

    private void useBucket(double x, double y) {
        int pixX = (int) x, pixY = (int) y;
        paintingData.fill(pixX, pixY, colors[activeSlot].getRGB(), opacity, toolParams[3], transBlend);
    }

    private void useEraser(double x, double y) {
        int pixX = (int) x, pixY = (int) y;
        for (int i = -toolParams[2]; i <= toolParams[2]; i++)
            for (int j = -toolParams[2]; j <= toolParams[2]; j++)
                if (!roundBrush || i * i + j * j <= toolParams[2] * toolParams[2])
                    paintingData.clearPixel(pixX + i, pixY + j);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double d2) {
        if (isOnToolParam(x, y)) {
            debugText = "Param: " + x + ", " + y + ", " + d2;
            toolParams[activeTool] += d2;
            toolParams[activeTool] = Mth.clamp(toolParams[activeTool], 0, MAX_PARAMS[activeTool]);
        } else {
            debugText = "Scale: " + x + ", " + y + ", " + d2;
            double areax = x - this.leftPos - PAINTING_LEFT;
            double areay = y - this.topPos - PAINTING_TOP;
            double oldpixsize = pixsize;
            pixsize += d2;
            pixsize = Math.min(Math.max(1, pixsize), 10);
            double scale = pixsize / oldpixsize;
            double dx = (areax - hor) * scale;
            double dy = (areay - ver) * scale;
            hor = areax - dx;
            ver = areay - dy;
        }
        return super.mouseScrolled(x, y, d2);
    }

    private boolean isOnPainting(double x, double y) {
        double dx = x - this.leftPos - PAINTING_LEFT;
        double dy = y - this.topPos - PAINTING_TOP;
        return dx >= 0.0D && dy >= 0.0D && dx <= PAINTING_WIDTH && dy <= PAINTING_HEIGHT;
    }

    private boolean updateActiveSlot(double x, double y) {
        for (int idx = 0; idx < 6; idx++) {
            int l = this.leftPos + 34 + 18 * idx;
            int r = l + 16;
            int t = this.topPos + 13;
            int b = t + 16;
            if (x >= l && x <= r && y >= t && y <= b && this.activeSlot != idx) {
                this.activeSlot = (byte) idx;
                return true;
            }
        }
        return false;
    }

    private boolean isOnToolParam(double x, double y) {
        return isOn(x - 216, y - TOOLS_TOP, 33, 16);
    }

    private boolean isOnOpacityPicker(double x, double y) {
        double dx = x - this.leftPos - OPACITY_LEFT;
        double dy = y - this.topPos - OPACITY_TOP;
        return dx >= 0.0D && dy >= 0.0D && dx <= OPACITY_WIDTH && dy <= OPACITY_HEIGHT;
    }

    private void getOpacity(double x, double y) {
        opacity = (int) ((1.0D - (y - this.topPos - OPACITY_TOP) / (double) OPACITY_HEIGHT) * 255.0D);
        this.opacityPos = (int) y - this.topPos;
    }

    @Override
    public boolean isPauseScreen() {
        return false; // returns true by default... interesting...
    }
}