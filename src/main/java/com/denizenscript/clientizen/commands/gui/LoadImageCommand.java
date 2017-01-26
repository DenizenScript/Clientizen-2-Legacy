package com.denizenscript.clientizen.commands.gui;

import com.denizenscript.clientizen.Clientizen;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoadImageCommand extends AbstractCommand {

    // <--[command]
    // @Name loadimage
    // @Arguments <id> <path or url>
    // @Short loads an image from a local path or URL.
    // @Updated 2017/01/25
    // @Group GUI
    // @Minimum 2
    // @Maximum 2
    // @Description
    // Downloads or loads an image into memory from a URL or a local file inside the /denizen2sponge/images folder.
    // The ID given to this command can be used in other commands, such as <@link command overlayimage>OverlayImage<@/link>.
    // To unload the image from memory, use <@link command unloadimage>UnloadImage<@/link>.
    // @Example
    // # This example loads an image from /denizen2sponge/images/local_image.png as MyImage.
    // - loadimage MyImage local_image.png
    // @Example
    // # This example downloads an image from a website.
    // - loadimage DownloadedImage http://example.com/image.jpg
    // -->

    @Override
    public String getName() {
        return "loadimage";
    }

    @Override
    public String getArguments() {
        return "<id> <path or url>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 2;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        String id = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 0)).getInternal();
        if (isImageLoaded(id)) {
            queue.handleError(entry, "The image ID '" + id + "' is already in use!");
            return;
        }
        String location = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 1)).getInternal();
        BufferedImage image;
        try {
            String lowerLoc = CoreUtilities.toLowerCase(location);
            if (lowerLoc.startsWith("http://") || lowerLoc.startsWith("https://")) {
                try {
                    URL url = new URL(location);
                    image = ImageIO.read(url);
                }
                catch (MalformedURLException e) {
                    queue.handleError(entry, "Invalid URL specified: " + location);
                    return;
                }
            }
            else {
                File file = new File(Clientizen.instance.imagesFolder, location);
                if (!file.getCanonicalPath().startsWith(Clientizen.instance.imagesFolder.getCanonicalPath())) {
                    queue.handleError(entry, "Invalid file path specified! You must stay within the /images folder!");
                    return;
                }
                image = ImageIO.read(file);
            }
        }
        catch (IOException e) {
            queue.handleError(entry, "Failed to load image due to an I/O exception!");
            return;
        }
        if (image == null) {
            queue.handleError(entry, "Failed to load image! No exception, but it was null!");
            return;
        }
        DynamicTexture texture = new DynamicTexture(image);
        String textureName = Clientizen.MOD_ID + "_image_" + id;
        ResourceLocation resource = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(textureName, texture);
        loadedImages.put(CoreUtilities.toLowerCase(id), resource);
    }

    private static final Map<String, ResourceLocation> loadedImages = new HashMap<>();

    public static void unloadImage(String name) {
        name = CoreUtilities.toLowerCase(name);
        if (loadedImages.containsKey(name)) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(loadedImages.get(name));
            loadedImages.remove(name);
            Clientizen.instance.overlayGuiHandler.removeImages(name);
        }
    }

    public static boolean isImageLoaded(String name) {
        return loadedImages.containsKey(CoreUtilities.toLowerCase(name));
    }

    public static ResourceLocation getLoadedImage(String name) {
        name = CoreUtilities.toLowerCase(name);
        if (loadedImages.containsKey(name)) {
            return loadedImages.get(name);
        }
        return null;
    }
}
