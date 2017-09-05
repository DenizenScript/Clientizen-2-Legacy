package com.denizenscript.clientizen.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class OverlayText extends OverlayGui {

    public String text;
    public int rgbColor;
    public boolean shadow;

    public OverlayText(float x, float y, String text, int rgbColor, boolean shadow) {
        super(x, y);
        this.text = text;
        this.rgbColor = rgbColor;
        this.shadow = shadow;
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res) {
        mc.fontRenderer.drawString(text, x / res.getScaleFactor(), y / res.getScaleFactor(), rgbColor, shadow);
    }
}
