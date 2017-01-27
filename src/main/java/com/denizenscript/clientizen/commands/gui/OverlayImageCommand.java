package com.denizenscript.clientizen.commands.gui;

import com.denizenscript.clientizen.Clientizen;
import com.denizenscript.clientizen.gui.OverlayGuiHandler;
import com.denizenscript.clientizen.gui.overlay.OverlayGui;
import com.denizenscript.clientizen.gui.overlay.OverlayImage;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import net.minecraft.util.ResourceLocation;

public class OverlayImageCommand extends AbstractCommand {

    // <--[command]
    // @Name overlayimage
    // @Arguments 'add'/'update'/'remove' <id> <image> <x> <y> <width> <height> <textureX> <textureY>
    // @Short shows an image on the player's in-game overlay.
    // @Updated 2017/01/27
    // @Group GUI
    // @Minimum 2
    // @Maximum 9
    // @Description
    // Shows an image on the player's in-game overlay.
    // The image must be loaded with the <@link command loadimage>LoadImage<@/link> command.
    // An ID is required for all actions in this command.
    // When using 'add', you must specify all arguments.
    // When using 'update', you may specify any number of arguments (still requires the ID).
    // When using 'remove', you should only specify the ID.
    // Note that if you unload an image using <@link command unloadimage>UnloadImage<@/link>, any existing
    // overlay image will automatically be removed.
    // @Example
    // # This example displays a loaded image called MyImage at the top-left corner of the screen.
    // - overlayimage add image1 MyImage 0 0 25 25 0 0
    // @Example
    // # This example updates 'image1' to move further right and changes the displayed image.
    // - overlayimage update image1 -x 50 -image MyOtherImage
    // @Example
    // # This example removes 'image1' from the screen.
    // - overlayimage remove image1
    // -->

    @Override
    public String getName() {
        return "overlayimage";
    }

    @Override
    public String getArguments() {
        return "'add'/'update'/'remove' <id> <image> <x> <y> <width> <height> <textureX> <textureY>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 9;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        Action action;
        try {
            action = Action.valueOf(TextTag.getFor(queue.error, entry.getArgumentObject(queue, 0)).getInternal().toUpperCase());
        }
        catch (IllegalArgumentException e) {
            queue.handleError(entry, "Invalid action! Valid: ADD, UPDATE, REMOVE.");
            return;
        }
        String id = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 1)).getInternal();
        OverlayGuiHandler overlay = Clientizen.instance.overlayGuiHandler;
        switch (action) {
            case ADD:
                if (overlay.contains(id)) {
                    queue.handleError(entry, "A GUI element with the ID '" + id + "' is already showing!");
                    return;
                }
                if (entry.arguments.size() < 9) {
                    queue.handleError(entry, "Must specify all arguments when adding!");
                    return;
                }
                String imageId = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 2)).getInternal();
                if (!LoadImageCommand.isImageLoaded(imageId)) {
                    queue.handleError(entry, "There is no loaded image with the specified ID: " + imageId);
                    return;
                }
                ResourceLocation image = LoadImageCommand.getLoadedImage(imageId);
                String imageName = CoreUtilities.toLowerCase(imageId);
                IntegerTag xTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 3));
                IntegerTag yTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 4));
                IntegerTag widthTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 5));
                IntegerTag heightTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 6));
                IntegerTag textureXTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 7));
                IntegerTag textureYTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 8));
                overlay.add(id, new OverlayImage(xTag.getInternal(), yTag.getInternal(), image, imageName,
                        widthTag.getInternal(), heightTag.getInternal(),
                        textureXTag.getInternal(), textureYTag.getInternal()));
                break;
            case UPDATE:
                if (!overlay.contains(id)) {
                    queue.handleError(entry, "No GUI element with the ID '" + id + "' exists!");
                    return;
                }
                OverlayGui gui = overlay.get(id);
                if (!(gui instanceof OverlayImage)) {
                    queue.handleError(entry, "A GUI element with the ID '" + id + "' exists, but it is not an image!");
                    return;
                }
                OverlayImage overlayImage = (OverlayImage) gui;
                if (entry.namedArgs.containsKey("image")) {
                    String imgId = TextTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "image")).getInternal();
                    if (!LoadImageCommand.isImageLoaded(imgId)) {
                        queue.handleError(entry, "There is no loaded image with the specified ID: " + imgId);
                        return;
                    }
                    overlayImage.image = LoadImageCommand.getLoadedImage(imgId);
                    overlayImage.imageName = CoreUtilities.toLowerCase(imgId);
                }
                if (entry.namedArgs.containsKey("x")) {
                    overlayImage.x = IntegerTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "x")).getInternal();
                }
                if (entry.namedArgs.containsKey("y")) {
                    overlayImage.y = IntegerTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "y")).getInternal();
                }
                if (entry.namedArgs.containsKey("width")) {
                    overlayImage.width = IntegerTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "width")).getInternal();
                }
                if (entry.namedArgs.containsKey("height")) {
                    overlayImage.height = IntegerTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "height")).getInternal();
                }
                if (entry.namedArgs.containsKey("textureX")) {
                    overlayImage.textureX = IntegerTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "textureX")).getInternal();
                }
                if (entry.namedArgs.containsKey("textureY")) {
                    overlayImage.textureY = IntegerTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "textureY")).getInternal();
                }
                break;
            case REMOVE:
                if (!overlay.contains(id)) {
                    queue.handleError(entry, "No GUI element with the ID '" + id + "' exists!");
                    return;
                }
                if (!(overlay.get(id) instanceof OverlayImage)) {
                    queue.handleError(entry, "A GUI element with the ID '" + id + "' exists, but it is not an image!");
                    return;
                }
                overlay.remove(id);
                break;
        }
    }

    private enum Action {
        ADD, UPDATE, REMOVE
    }
}
