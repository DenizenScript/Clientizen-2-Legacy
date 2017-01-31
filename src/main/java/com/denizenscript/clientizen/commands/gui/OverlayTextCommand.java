package com.denizenscript.clientizen.commands.gui;

import com.denizenscript.clientizen.Clientizen;
import com.denizenscript.clientizen.gui.OverlayGuiHandler;
import com.denizenscript.clientizen.gui.overlay.OverlayGui;
import com.denizenscript.clientizen.gui.overlay.OverlayText;
import com.denizenscript.clientizen.tags.objects.ColorTag;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;

public class OverlayTextCommand extends AbstractCommand {

    // <--[command]
    // @Name overlaytext
    // @Arguments 'add'/'update'/'remove' <id> <text> <x> <y> <color> <shadow>
    // @Short shows text on the player's in-game overlay.
    // @Updated 2017/01/27
    // @Group GUI
    // @Minimum 2
    // @Maximum 7
    // @Description
    // Shows a string of text on the player's in-game overlay.
    // An ID is required for all actions in this command.
    // When using 'add', you must specify all arguments.
    // When using 'update', you may specify any number of arguments (still requires the ID).
    // When using 'remove', you should only specify the ID.
    // @Example
    // # This adds simple red GUI text to the top-left of the screen.
    // - overlaytext add helloworld "Hello World!" 10 10 red false
    // @Example
    // # This updates 'helloworld' to be a different color and have weird casing.
    // - overlaytext update helloworld -color blue -text "heLLo wORld!"
    // @Example
    // # This removes the 'helloworld' text from the screen.
    // - overlaytext remove helloworld
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
        OverlayGuiHandler overlay = Clientizen.instance.overlayGuiHandler;
        switch (action) {
            case ADD:
                if (overlay.contains(id)) {
                    queue.handleError(entry, "A GUI element with the ID '" + id + "' is already showing!");
                    return;
                }
                if (entry.arguments.size() < 7) {
                    queue.handleError(entry, "Must specify all arguments when adding!");
                    return;
                }
                TextTag textTag = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 2));
                IntegerTag xTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 3));
                IntegerTag yTag = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 4));
                ColorTag colorTag = ColorTag.getFor(queue.error, entry.getArgumentObject(queue, 5));
                BooleanTag shadowTag = BooleanTag.getFor(queue.error, entry.getArgumentObject(queue, 6));
                overlay.add(id, new OverlayText((int) xTag.getInternal(), (int) yTag.getInternal(), textTag.getInternal(),
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
                if (entry.namedArgs.containsKey("text")) {
                    overlayText.text = TextTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "text")).getInternal();
                }
                if (entry.namedArgs.containsKey("x")) {
                    overlayText.x = IntegerTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "x")).getInternal();
                }
                if (entry.namedArgs.containsKey("y")) {
                    overlayText.y = IntegerTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "y")).getInternal();
                }
                if (entry.namedArgs.containsKey("color")) {
                    overlayText.rgbColor = ColorTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "color")).getInternal().getRGB();
                }
                if (entry.namedArgs.containsKey("shadow")) {
                    overlayText.shadow = BooleanTag.getFor(queue.error, entry.getNamedArgumentObject(queue, "shadow")).getInternal();
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
