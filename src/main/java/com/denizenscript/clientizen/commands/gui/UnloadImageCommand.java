package com.denizenscript.clientizen.commands.gui;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.TextTag;

public class UnloadImageCommand extends AbstractCommand {

    // <--[command]
    // @Name unloadimage
    // @Arguments <id>
    // @Short unloads an image from memory.
    // @Updated 2017/01/25
    // @Group GUI
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Unloads a previously loaded image from memory, removing the ability to use it further.
    // To load an image from memory, use <@link command loadimage>LoadImage<@/link>.
    // @Example
    // # This example unloads an image called MyImage.
    // - unloadimage MyImage
    // -->

    @Override
    public String getName() {
        return "unloadimage";
    }

    @Override
    public String getArguments() {
        return "<id>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 1;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        String id = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 0)).getInternal();
        if (!LoadImageCommand.isImageLoaded(id)) {
            queue.handleError(entry, "No image with the ID '" + id + "' is loaded!");
            return;
        }
        LoadImageCommand.unloadImage(id);
    }
}
