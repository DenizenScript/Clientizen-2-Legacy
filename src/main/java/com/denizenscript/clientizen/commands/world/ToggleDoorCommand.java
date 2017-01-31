package com.denizenscript.clientizen.commands.world;

import com.denizenscript.clientizen.tags.objects.PositionTag;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToggleDoorCommand extends AbstractCommand {

    // <--[command]
    // @Name toggledoor
    // @Arguments <position> [toggled]
    // @Short toggles a door opened or closed.
    // @Updated 2017/01/31
    // @Group World
    // @Minimum 1
    // @Maximum 2
    // @Description
    // Toggles a the open state of a door.
    // @Example
    // # This example opens a door in front of the player.
    // - toggledoor <client.cursor_on> true
    // -->

    @Override
    public String getName() {
        return "toggledoor";
    }

    @Override
    public String getArguments() {
        return "<position> [toggled]";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 2;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        BlockPos position = PositionTag.getFor(queue.error, entry.getArgumentObject(queue, 0)).getInternal().toBlockPos();
        World world = Minecraft.getMinecraft().world;
        Block block = world.getBlockState(position).getBlock();
        if (!(block instanceof BlockDoor)) {
            queue.handleError(entry, "Specified block is not a door!");
            return;
        }
        boolean open;
        if (entry.arguments.size() > 1) {
            open = BooleanTag.getFor(queue.error, entry.getArgumentObject(queue, 1)).getInternal();
        }
        else {
            open = !BlockDoor.isOpen(world, position);
        }
        ((BlockDoor) block).toggleDoor(world, position, open);
    }
}
