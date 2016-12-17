package com.denizenscript.clientizen.commands.entity;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.NumberTag;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ScalePlayerCommand extends AbstractCommand {

    // <--[command]
    // @Name scaleplayer
    // @Arguments <size>
    // @Short rescales the local player.
    // @Updated 2016/12/16
    // @Group Entity
    // @Minimum 1
    // @Maximum 1
    // @Warning This command exists for experimental reasons only!
    // @Description
    // Rescales the local player.
    // @Example
    // # This example scales the player to half size.
    // - scaleplayer 0.5
    // -->

    @Override
    public String getName() {
        return "scaleplayer";
    }

    @Override
    public String getArguments() {
        return "<size>";
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
        if (mainScs != null) {
            mainScs.disable();
            mainScs = null;
        }
        ScalePlayerSystem scs = new ScalePlayerSystem();
        scs.size = (float) NumberTag.getFor(queue.error, entry.getArgumentObject(queue, 0)).getInternal();
        if (scs.size == 1.0f) {
            if (queue.shouldShowGood()) {
                queue.outGood("Ignoring a non-editing scaler!");
            }
            return;
        }
        scs.register();
        mainScs = scs;
        if (queue.shouldShowGood()) {
            queue.outGood("Registered a scaler!");
        }
    }

    public static ScalePlayerSystem mainScs;

    public class ScalePlayerSystem {

        public float size;

        public void register() {
            MinecraftForge.EVENT_BUS.register(this);
        }

        public void disable() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }

        @SubscribeEvent
        public void renderEntityPre(RenderPlayerEvent.Pre event) {
            for (ModelRenderer renderer : event.getRenderer().getMainModel().boxList) {
                renderer.offsetX *= size;
                renderer.offsetY *= size;
                renderer.offsetZ *= size;
                List<ModelBox> neo = new ArrayList<>();
                for (ModelBox box : renderer.cubeList) {
                    neo.add(new ModelBox(renderer, 0, 0, box.posX1 * size, box.posY1 * size, box.posZ1 * size,
                            (int)((box.posZ2 - box.posX1) * size), (int)((box.posY2 - box.posY1) * size), (int)((box.posZ2 - box.posZ1) * size), 0));
                }
                renderer.cubeList = neo;
            }
        }
    }
}
