package com.denizenscript.clientizen.commands.local;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import net.minecraft.client.Minecraft;

public class ExecuteCommand extends AbstractCommand {

    // <--[command]
    // @Name execute
    // @Arguments <message or command>
    // @Short executes a command as the local client.
    // @Updated 2016/12/16
    // @Group Local
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Executes a command as the local client.
    // @Example
    // # This example chats the message 'Hello'.
    // - execute "Hello"
    // @Example
    // # This executes the command 'help'.
    // - execute "/help"
    // -->

    @Override
    public String getName() {
        return "execute";
    }

    @Override
    public String getArguments() {
        return "<message or command>";
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
        Minecraft.getMinecraft().player.sendChatMessage(entry.getArgumentObject(queue, 0).toString());
    }
}
