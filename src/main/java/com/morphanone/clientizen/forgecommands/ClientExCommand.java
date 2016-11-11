package com.morphanone.clientizen.forgecommands;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.morphanone.clientizen.util.SimpleCommandSender;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClientExCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "clientex";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cex dCommand";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("cex");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        List<String> argset = new ArrayList<>(Arrays.asList(args));
        boolean quiet = false;
        if (argset.size() > 0 && argset.get(0).equals("-q")) {
            quiet = true;
            argset.remove(0);
        }
        String cmd = CoreUtilities.concat(argset, " ");
        // TODO: Redirect output to the commandSource!
        MapTag defs = new MapTag();
        Denizen2Core.runString(cmd, defs, quiet ? null : new SimpleCommandSender(sender));
    }
}
