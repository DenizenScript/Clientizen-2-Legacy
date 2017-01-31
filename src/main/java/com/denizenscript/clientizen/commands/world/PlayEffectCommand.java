package com.denizenscript.clientizen.commands.world;

import com.denizenscript.clientizen.tags.objects.PositionTag;
import com.denizenscript.clientizen.util.UtilPosition;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.tags.objects.NumberTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;

public class PlayEffectCommand extends AbstractCommand {

    // <--[command]
    // @Name playeffect
    // @Arguments <position> <effect> [count] [offset] [speed]
    // @Short plays an effect.
    // @Updated 2017/01/30
    // @Group World
    // @Minimum 2
    // @Maximum 5
    // @Description
    // Shows a particle effect at the specified position.
    // You can specify the count number to show more particles, the offset vector to
    // spread out the particles, and the speed number for particle movement.
    // @Example
    // # This example plays the 'heart' effect around the player.
    // - playeffect <client.position> heart 50 1,1,1 0
    // -->

    @Override
    public String getName() {
        return "playeffect";
    }

    @Override
    public String getArguments() {
        return "<position> <effect> [count] [offset] [speed]";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 5;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        UtilPosition pos = PositionTag.getFor(queue.error, entry.getArgumentObject(queue, 0)).getInternal();
        String effectName = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 1)).getInternal();
        EnumParticleTypes type;
        try {
            type = EnumParticleTypes.valueOf(CoreUtilities.toUpperCase(effectName));
        }
        catch (IllegalArgumentException e) {
            queue.handleError(entry, "Invalid particle effect type: '" + effectName + "'!");
            return;
        }
        int size = entry.arguments.size();
        long count = 1;
        UtilPosition offset = new UtilPosition();
        double speed = 0.0f;
        if (size > 2) {
            count = IntegerTag.getFor(queue.error, entry.getArgumentObject(queue, 2)).getInternal();
        }
        if (size > 3) {
            offset = PositionTag.getFor(queue.error, entry.getArgumentObject(queue, 3)).getInternal();
        }
        if (size > 4) {
            speed = NumberTag.getFor(queue.error, entry.getArgumentObject(queue, 4)).getInternal();
        }
        if (count == 0) {
            double xOff = speed * offset.x;
            double yOff = speed * offset.y;
            double zOff = speed * offset.z;
            Minecraft.getMinecraft().world.spawnParticle(type, true, pos.x, pos.y, pos.z, xOff, yOff, zOff);
        }
        else {
            for (long i = 0; i < count; i++) {
                double xOff = CoreUtilities.random.nextGaussian() * offset.x;
                double yOff = CoreUtilities.random.nextGaussian() * offset.y;
                double zOff = CoreUtilities.random.nextGaussian() * offset.z;
                double xSpeed = CoreUtilities.random.nextGaussian() * speed;
                double ySpeed = CoreUtilities.random.nextGaussian() * speed;
                double zSpeed = CoreUtilities.random.nextGaussian() * speed;
                Minecraft.getMinecraft().world.spawnParticle(type, true, pos.x + xOff, pos.y + yOff, pos.z + zOff, xSpeed, ySpeed, zSpeed);
            }
        }
    }
}
