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
    // @Arguments 'add'/'update'/'remove' <id> <x> <y> <width> <height> <textureX> <textureY> <image>
    // @Short shows an image on the player's in-game overlay.
    // @Updated 2016/11/09
    // @Group GUI
    // @Minimum 2
    // @Maximum 9
    // @Description
    // Shows an image on the player's in-game overlay.
    // The image must be loaded with the <@link command loadimage>LoadImage<@/link> command.
    // An ID is required for all actions in this command.
    // When using 'add', you must specify all arguments.
    // When using 'update', you may specify any number of arguments (still requires an ID).
    // When using 'remove', you should only specify the ID.
    // @Example
    // # This example displays a loaded image called MyImage at the top-left corner of the screen.
    // - overlayimage add image1 0 0 25 25 0 0 MyImage
    // -->

    @Override
    public String getName() {
        return "overlayimage";
    }

    @Override
    public String getArguments() {
        return "'add'/'update'/'remove' <id> <x> <y> <width> <height> <textureX> <textureY> <image>";
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
        IntegerTag xTag = null;
        IntegerTag yTag = null;
        IntegerTag widthTag = null;
        IntegerTag heightTag = null;
        IntegerTag textureXTag = null;
        IntegerTag textureYTag = null;
        ResourceLocation image = null;
        String imageName = null;
        int size = entry.arguments.size();
        if (size > 2) {
            xTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 2));
            if (size > 3) {
                yTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 3));
                if (size > 4) {
                    widthTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 4));
                    if (size > 5) {
                        heightTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 5));
                        if (size > 6) {
                            textureXTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 6));
                            if (size > 7) {
                                textureYTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 7));
                                if (size > 8) {
                                    String imageId = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 8)).getInternal();
                                    if (!LoadImageCommand.isImageLoaded(imageId)) {
                                        queue.handleError(entry, "There is no loaded image with the specified ID: " + imageId);
                                        return;
                                    }
                                    image = LoadImageCommand.getLoadedImage(imageId);
                                    imageName = CoreUtilities.toLowerCase(imageId);
                                }
                            }
                        }
                    }
                }
            }
        }
        OverlayGuiHandler overlay = Clientizen.instance.overlayGuiHandler;
        switch (action) {
            case ADD:
                if (overlay.contains(id)) {
                    queue.handleError(entry, "A GUI element with the ID '" + id + "' is already showing!");
                    return;
                }
                if (xTag == null || yTag == null || widthTag == null || heightTag == null
                        || textureXTag == null || textureYTag == null || image == null) {
                    queue.handleError(entry, "Must specify all arguments when adding!");
                    return;
                }
                overlay.add(id, new OverlayImage((int) xTag.getInternal(), (int) yTag.getInternal(), image, imageName,
                        (int) widthTag.getInternal(), (int) heightTag.getInternal(),
                        (int) textureXTag.getInternal(), (int) textureYTag.getInternal()));
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
                if (xTag != null) {
                    overlayImage.x = (int) xTag.getInternal();
                }
                if (yTag != null) {
                    overlayImage.y = (int) yTag.getInternal();
                }
                if (widthTag != null) {
                    overlayImage.width = (int) widthTag.getInternal();
                }
                if (heightTag != null) {
                    overlayImage.height = (int) heightTag.getInternal();
                }
                if (textureXTag != null) {
                    overlayImage.textureX = (int) textureXTag.getInternal();
                }
                if (textureYTag != null) {
                    overlayImage.textureY = (int) textureYTag.getInternal();
                }
                if (image != null) {
                    overlayImage.image = image;
                    overlayImage.imageName = imageName;
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
