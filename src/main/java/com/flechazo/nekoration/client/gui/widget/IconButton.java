package com.flechazo.nekoration.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class IconButton extends Button {
    private ResourceLocation iconResource;
    private int iconU;
    private int iconV;

    public IconButton(int x, int y, Component message, OnPress pressable, ResourceLocation iconResource, int iconU, int iconV) {
        super(x, y, 20, 20, message, pressable, DEFAULT_NARRATION);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    public void setIcon(ResourceLocation iconResource, int iconU, int iconV) {
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        int offset;
        if (!this.active) {
            offset = 0;
        } else if (this.isHovered) {
            offset = 2;
        } else {
            offset = 1;
        }

        int x = this.getX();
        int y = this.getY();

        guiGraphics.blit(WIDGETS_LOCATION, x, y, 0, 46 + offset * 20, width / 2, height);
        guiGraphics.blit(WIDGETS_LOCATION, x + width / 2, y, 200 - width / 2, 46 + offset * 20, width / 2, height);

        if (!this.active) {
            RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 1.0F);
        }
        RenderSystem.setShaderTexture(0, this.iconResource);

        guiGraphics.blit(this.iconResource, x + 2, y + 2, iconU, iconV, 16, 16);
    }
}