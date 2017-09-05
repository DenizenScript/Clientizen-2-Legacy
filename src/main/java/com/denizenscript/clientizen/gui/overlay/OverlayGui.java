package com.denizenscript.clientizen.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class OverlayGui {

    public float x;
    public float y;

    public OverlayGui(float x, float y) {
        this.x = x;
        this.y = y;
    }

    protected static void drawTexturedRect(float x, float y, float width, float height, float textureX, float textureY) {
        float f = 1.0F / width;
        float f1 = 1.0F / height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x, y + height, 0).tex(textureX * f, textureY + height * f1).endVertex();
        vertexbuffer.pos(x + width, y + height, 0).tex(textureX + width * f, textureY + height * f1).endVertex();
        vertexbuffer.pos(x + width, y, 0).tex(textureX + width * f, textureY * f1).endVertex();
        vertexbuffer.pos(x, y, 0).tex(textureX * f, textureY * f1).endVertex();
        tessellator.draw();
    }

    public abstract void render(Minecraft mc, ScaledResolution res);
}
