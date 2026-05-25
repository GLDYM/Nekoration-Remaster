package com.flechazo.nekoration.client.gui.screen;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.Nekoration;
import com.flechazo.nekoration.blocks.containers.EaselMenuMenu;
import com.flechazo.nekoration.client.gui.widget.IconButton;
import com.flechazo.nekoration.network.C2SUpdateEaselMenuData;
import com.flechazo.nekoration.network.ModPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Objects;

public class EaselMenuScreen extends AbstractContainerScreen<EaselMenuMenu> {
    private final EditBox[] textInputs = new EditBox[8];

    private IconButton glowButton;

    private static final int COLOR_NUM = DyeColor.values().length;
    private static final float[][] COLOR_SET = new float[COLOR_NUM][3];

    private int selectedColor = DyeColor.WHITE.getId();
    private int editingText = 0;

    public boolean showColorPicker = false;
    private final Component tipMessage1;
    private final Component tipMessage2;

    public EaselMenuScreen(EaselMenuMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        // Set the width and height of the gui. Should match the size of the texture!
        imageWidth = 176;
        imageHeight = 222;
        for (int i = 0; i < COLOR_NUM; i++) {
            int color = DyeColor.byId(i).getTextColor();
            COLOR_SET[i][0] = NekoColors.getRedf(color);
            COLOR_SET[i][1] = NekoColors.getGreenf(color);
            COLOR_SET[i][2] = NekoColors.getBluef(color);
        }
        tipMessage1 = Component.translatable("gui.nekoration.message.press_key_color_picker_on", "'F1'");
        tipMessage2 = Component.translatable("gui.nekoration.message.press_key_color_picker_off", "'F1'");
    }

    @Override
    public void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        final int extraOffsetX = 14;
        final int extraOffsetY = 10;
        for (int i = 0; i < 8; i++) {
            this.textInputs[i] = new EditBox(this.font, this.leftPos + extraOffsetX + (i < 4 ? 8 : 98), this.topPos + extraOffsetY + 36 + (i % 4) * 18, 70, 18, Component.translatable("gui.nekoration.color"));
            this.textInputs[i].setMaxLength(8);
            final int j = i;
            this.textInputs[j].setResponder(input -> {
                this.menu.easel.setMessage(j, Component.nullToEmpty(input));
            });
            this.textInputs[i].setVisible(true);
            this.textInputs[i].setTextColor(this.menu.easel.getColor(i).getTextColor());
            this.textInputs[i].setTextColorUneditable(DyeColor.LIGHT_GRAY.getTextColor());
            this.textInputs[i].setBordered(false);
            this.textInputs[i].setValue(this.menu.easel.getMessage(i).getString());
            this.addWidget(this.textInputs[i]);
        }
        final Component enableGlow = Component.translatable("gui.nekoration.button.enable_glow");
        final Component disableGlow = Component.translatable("gui.nekoration.button.disable_glow");

        glowButton = new IconButton(leftPos + imageWidth + 2, topPos + 4, menu.easel.getGlowing() ? disableGlow : enableGlow, button -> {
            boolean glow = menu.easel.toggleGlowing();
            button.setMessage(glow ? disableGlow : enableGlow);
            ((IconButton) button).setIcon(ICONS, glow ? 0 : 16, 0);
        }, ICONS, menu.easel.getGlowing() ? 0 : 16, 0);
        this.addWidget(glowButton);
        this.setFocused(this.textInputs[0]);
    }

    @Override
    public void setFocused(@Nullable GuiEventListener widgeti) {
        super.setFocused(widgeti);
        if (getFocused() instanceof EditBox widget) {
            for (int i = 0; i < textInputs.length; i++) {
                if (textInputs[i] == widget) {
                    editingText = i;
                    selectedColor = menu.easel.getColor(i).getId();
                } else if (textInputs[i].isFocused()) {
                    textInputs[i].setFocused(false);
                }
            }
        }
    }

    @Override
    public void containerTick() {
        for (int i = 0; i < 8; i++) {
            textInputs[i].tick();
        }
    }

    @Override
    public void onClose() {
        //Send a packet to the Server tu update data...
        final C2SUpdateEaselMenuData packet = new C2SUpdateEaselMenuData(menu.easel.getBlockPos(), menu.easel.getMessages(), menu.easel.getColors(), menu.easel.getGlowing());
        ModPacketHandler.CHANNEL.sendToServer(packet);
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_E:
                return true;
            case GLFW.GLFW_KEY_ESCAPE:
                Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player).closeContainer();
                return true;
            case GLFW.GLFW_KEY_F1:
                showColorPicker = !showColorPicker;
                return true;
        }
        boolean res = super.keyPressed(keyCode, scanCode, modifier);
        for (EditBox input : textInputs) {
            res |= input.canConsumeInput();
        }
        return res;
    }

    @Override
    public boolean mouseClicked(double x, double y, int type) {
        // Call the super type's first because it changes focused control...
        boolean res = super.mouseClicked(x, y, type);
        if (showColorPicker)
            for (int i = 0; i < COLOR_NUM; i++) {
                if (i == selectedColor)
                    continue;
                if (isOn(x + 16, y - 8 - i * 14, 12, 12)) {
                    menu.easel.setColor(editingText, DyeColor.byId(i));
                    textInputs[editingText].setTextColor(DyeColor.byId(i).getTextColor());
                    selectedColor = i;
                    setFocused(textInputs[editingText]);
                    textInputs[editingText].setFocused(true);
                }
            }
        return res;
    }

    private boolean isOn(double x, double y, double w, double h) {
        double dx = x - this.leftPos;
        double dy = y - this.topPos;
        return dx >= 0.0D && dy >= 0.0D && dx <= w && dy <= h;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, menu.easel.white ? WHITE_BACKGROUND : BACKGROUND);
        if (showColorPicker) {
            for (int i = 0; i < COLOR_NUM; i++) {
                RenderSystem.setShaderColor(COLOR_SET[i][0], COLOR_SET[i][1], COLOR_SET[i][2], 1.0F);
                guiGraphics.blit(BACKGROUND, leftPos - 16 - (i == selectedColor ? 4 : 0), topPos + 8 + i * 14, 0, 240, 12, 12);
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        for (int i = 0; i < 8; i++)
            this.textInputs[i].render(guiGraphics, mouseX, mouseY, partialTicks);
        glowButton.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (glowButton.isMouseOver(mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font, glowButton.getMessage(), mouseX, mouseY);
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        var pose = guiGraphics.pose();
        pose.pushPose();
        pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
        pose.translate(topPos + 30, -leftPos - 192, 0);
        guiGraphics.drawString(this.font, showColorPicker ? tipMessage2 : tipMessage1, 1, 1, (150 << 24) + (255 << 16) + (255 << 8) + 255, false);
        pose.popPose();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        final float LABEL_XPOS = 6;
        final float CHEST_LABEL_YPOS = EaselMenuMenu.TILE_INVENTORY_YPOS - 10;
        guiGraphics.drawString(this.font, this.title, (int) LABEL_XPOS, (int) CHEST_LABEL_YPOS, DyeColor.GRAY.getTextColor(), false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(menu.easel.white ? WHITE_BACKGROUND : BACKGROUND, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    // This is the resource location for the background image for the GUI
    private static final ResourceLocation ICONS = new ResourceLocation(Nekoration.MODID, "textures/gui/icons.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Nekoration.MODID, "textures/gui/easel_menu.png");
    private static final ResourceLocation WHITE_BACKGROUND = new ResourceLocation(Nekoration.MODID, "textures/gui/easel_menu_white.png");
}