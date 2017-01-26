package com.denizenscript.clientizen.commands.local;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatCommand extends AbstractCommand {

    // <--[command]
    // @Name chat
    // @Arguments <message>
    // @Short shows a message on the client chat.
    // @Updated 2016/12/16
    // @Group Local
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Shows a message on the client chat.
    // @Example
    // # This example shows the message 'hello'.
    // - chat "Hello"
    // -->

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public String getArguments() {
        return "<message>";
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
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(entry.getArgumentObject(queue, 0).toString()));
    }
}
