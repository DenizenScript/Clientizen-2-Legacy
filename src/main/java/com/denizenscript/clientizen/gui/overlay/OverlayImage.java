package com.denizenscript.clientizen.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class OverlayImage extends OverlayGui {

    public ResourceLocation image;
    public String imageName;
    public int width;
    public int height;
    public int textureX;
    public int textureY;

    public OverlayImage(int x, int y, ResourceLocation image, String imageName,
                        int width, int height, int textureX, int textureY) {
        super(x, y);
        this.image = image;
        this.imageName = imageName;
        this.width = width;
        this.height = height;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res) {
        GlStateManager.pushAttrib();
        mc.getTextureManager().bindTexture(image);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        drawTexturedRect(x, y, width, height, textureX, textureY);
        GlStateManager.popAttrib();
    }
}
