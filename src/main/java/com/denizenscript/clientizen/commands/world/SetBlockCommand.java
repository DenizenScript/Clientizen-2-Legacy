package com.denizenscript.clientizen.commands.world;

import com.denizenscript.clientizen.tags.objects.BlockTypeTag;
import com.denizenscript.clientizen.tags.objects.PositionTag;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.ListTag;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class SetBlockCommand extends AbstractCommand {

    // <--[command]
    // @Name setblock
    // @Arguments <list of positions> <blocktype>
    // @Short sets a block's type.
    // @Updated 2017/02/09
    // @Group World
    // @Minimum 2
    // @Maximum 2
    // @Description
    // Sets the block type of one or more provided positions.
    // @Example
    // # This example sets the block at a player's location to stone.
    // - setblock <client.position> minecraft:stone
    // -->

    @Override
    public String getName() {
        return "setblock";
    }

    @Override
    public String getArguments() {
        return "<list of locations> <blocktype>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 2;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        ListTag positions = ListTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
        BlockTypeTag type = BlockTypeTag.getFor(queue.error, entry.getArgumentObject(queue, 1));
        World world = Minecraft.getMinecraft().world;
        for (AbstractTagObject ato : positions.getInternal()) {
            PositionTag pos = PositionTag.getFor(queue.error, ato);
            world.setBlockState(pos.getInternal().toBlockPos(), type.getInternal().getDefaultState());
        }
    }
}
