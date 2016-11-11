package com.morphanone.clientizen.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class OverlayGui {

    public int x;
    public int y;

    public OverlayGui(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected static void drawTexturedRect(int x, int y, int width, int height, int textureX, int textureY) {
        float f = 1.0F / width;
        float f1 = 1.0F / height;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x, y + height, 0).tex(textureX * f, textureY + height * f1).endVertex();
        vertexbuffer.pos(x + width, y + height, 0).tex(textureX + width * f, textureY + height * f1).endVertex();
        vertexbuffer.pos(x + width, y, 0).tex(textureX + width * f, textureY * f1).endVertex();
        vertexbuffer.pos(x, y, 0).tex(textureX * f, textureY * f1).endVertex();
        tessellator.draw();
    }

    public abstract void render(Minecraft mc, ScaledResolution res);
}
