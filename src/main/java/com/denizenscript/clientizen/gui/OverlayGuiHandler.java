package com.denizenscript.clientizen.gui;

import com.denizenscript.clientizen.gui.overlay.OverlayGui;
import com.denizenscript.clientizen.gui.overlay.OverlayImage;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OverlayGuiHandler {

    private final Map<String, OverlayGui> guiById = new HashMap<String, OverlayGui>();
    private final Minecraft minecraft = Minecraft.getMinecraft();

    public void add(String id, OverlayGui gui) {
        id = CoreUtilities.toLowerCase(id);
        if (!guiById.containsKey(id)) {
            guiById.put(id, gui);
        }
    }

    public void remove(String id) {
        id = CoreUtilities.toLowerCase(id);
        if (guiById.containsKey(id)) {
            guiById.remove(id);
        }
    }

    public OverlayGui get(String id) {
        id = CoreUtilities.toLowerCase(id);
        if (guiById.containsKey(id)) {
            return guiById.get(id);
        }
        return null;
    }

    public boolean contains(String id) {
        return guiById.containsKey(CoreUtilities.toLowerCase(id));
    }

    // TODO: find a better way to do this?
    public void removeImages(String imageName) {
        imageName = CoreUtilities.toLowerCase(imageName);
        Iterator<Map.Entry<String, OverlayGui>> iter = guiById.entrySet().iterator();
        while (iter.hasNext()) {
            OverlayGui next = iter.next().getValue();
            if (next instanceof OverlayImage && ((OverlayImage) next).imageName.equals(imageName)) {
                iter.remove();
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        for (OverlayGui gui : guiById.values()) {
            gui.render(minecraft, event.getResolution());
        }
    }
}
