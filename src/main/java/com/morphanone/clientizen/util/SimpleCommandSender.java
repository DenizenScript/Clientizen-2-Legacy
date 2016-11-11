package com.morphanone.clientizen.util;

import com.denizenscript.denizen2core.utilities.AbstractSender;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

public class SimpleCommandSender extends AbstractSender {

    public ICommandSender commandSender;

    public SimpleCommandSender(ICommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Override
    public void sendColoredMessage(String message) {
        commandSender.addChatMessage(new TextComponentString(message));
    }
}
