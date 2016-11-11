package com.morphanone.clientizen.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class OverlayText extends OverlayGui {

    public String text;
    public int rgbColor;
    public boolean shadow;

    public OverlayText(int x, int y, String text, int rgbColor, boolean shadow) {
        super(x, y);
        this.text = text;
        this.rgbColor = rgbColor;
        this.shadow = shadow;
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res) {
        mc.fontRendererObj.drawString(text, x, y, rgbColor, shadow);
    }
}
