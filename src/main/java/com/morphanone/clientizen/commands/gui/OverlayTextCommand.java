package com.morphanone.clientizen.commands.gui;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.morphanone.clientizen.Clientizen;
import com.morphanone.clientizen.gui.OverlayGuiHandler;
import com.morphanone.clientizen.gui.overlay.OverlayGui;
import com.morphanone.clientizen.gui.overlay.OverlayText;
import com.morphanone.clientizen.tags.objects.ColorTag;

public class OverlayTextCommand extends AbstractCommand {

    // <--[command]
    // @Name overlaytext
    // @Arguments 'add'/'update'/'remove' <id> <text> <x> <y> <color> <shadow>
    // @Short shows text on the player's in-game overlay.
    // @Updated 2016/11/04
    // @Group GUI
    // @Minimum 2
    // @Maximum 7
    // @Description
    // Shows a string of text on the player's in-game overlay.
    // An ID is required for all actions in this command.
    // When using 'add', you must specify all arguments.
    // When using 'update', you may specify any number of arguments (still requires an ID).
    // When using 'remove', you should only specify the ID.
    // @Example
    // # This adds simple red GUI text to the top-left of the screen.
    // - overlaytext add helloworld "Hello World!" 10 10 red false
    // -->

    @Override
    public String getName() {
        return "overlaytext";
    }

    @Override
    public String getArguments() {
        return "'add'/'update'/'remove' <id> <text> <x> <y> <color> <shadow>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 7;
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
        String text = null;
        IntegerTag xTag = null;
        IntegerTag yTag = null;
        ColorTag colorTag = null;
        BooleanTag shadowTag = null;
        int size = entry.arguments.size();
        if (size > 2) {
            text = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 2)).getInternal();
            if (size > 3) {
                xTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 3));
                if (size > 4) {
                    yTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 4));
                    if (size > 5) {
                        colorTag = ColorTag.getFor(queue.error, entry.getArgumentObject(queue, 5));
                        if (size > 6) {
                            shadowTag = BooleanTag.getFor(queue.error, entry.getArgumentObject(queue, 6));
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
                if (text == null || xTag == null || yTag == null || colorTag == null || shadowTag == null) {
                    queue.handleError(entry, "Must specify all arguments when adding!");
                    return;
                }
                overlay.add(id, new OverlayText((int) xTag.getInternal(), (int) yTag.getInternal(), text,
                        colorTag.getInternal().getRGB(), shadowTag.getInternal()));
                break;
            case UPDATE:
                if (!overlay.contains(id)) {
                    queue.handleError(entry, "No GUI element with the ID '" + id + "' exists!");
                    return;
                }
                OverlayGui gui = overlay.get(id);
                if (!(gui instanceof OverlayText)) {
                    queue.handleError(entry, "A GUI element with the ID '" + id + "' exists, but it is not text!");
                    return;
                }
                OverlayText overlayText = (OverlayText) gui;
                if (text != null) {
                    overlayText.text = text;
                }
                if (xTag != null) {
                    overlayText.x = (int) xTag.getInternal();
                }
                if (yTag != null) {
                    overlayText.y = (int) yTag.getInternal();
                }
                if (colorTag != null) {
                    overlayText.rgbColor = colorTag.getInternal().getRGB();
                }
                if (shadowTag != null) {
                    overlayText.shadow = shadowTag.getInternal();
                }
                break;
            case REMOVE:
                if (!overlay.contains(id)) {
                    queue.handleError(entry, "No GUI element with the ID '" + id + "' exists!");
                    return;
                }
                if (!(overlay.get(id) instanceof OverlayText)) {
                    queue.handleError(entry, "A GUI element with the ID '" + id + "' exists, but it is not text!");
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
