package com.denizenscript.clientizen.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class OverlayImage extends OverlayGui {

    public ResourceLocation image;
    public String imageName;
    public float width;
    public float height;
    public float textureX;
    public float textureY;

    public OverlayImage(float x, float y, ResourceLocation image, String imageName,
                        float width, float height, float textureX, float textureY) {
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
        drawTexturedRect(x / res.getScaleFactor(), y / res.getScaleFactor(),
                width / res.getScaleFactor(), height / res.getScaleFactor(),
                textureX / res.getScaleFactor(), textureY / res.getScaleFactor());
        GlStateManager.popAttrib();
    }
}
