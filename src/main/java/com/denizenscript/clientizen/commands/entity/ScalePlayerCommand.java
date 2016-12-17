package com.denizenscript.clientizen.commands.entity;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.NumberTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
            Minecraft.getMinecraft().thePlayer.eyeHeight /= mainScs.size;
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
        scs.player = Minecraft.getMinecraft().thePlayer;
        Minecraft.getMinecraft().thePlayer.eyeHeight *= scs.size;
        scs.register();
        mainScs = scs;
        if (queue.shouldShowGood()) {
            queue.outGood("Registered a scaler!");
        }
    }

    public static ScalePlayerSystem mainScs;

    public class ScalePlayerSystem {

        public float size;

        public EntityPlayer player;

        public void register() {
            MinecraftForge.EVENT_BUS.register(this);
        }

        public void disable() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }

        @SubscribeEvent
        public void renderEntityPre(RenderLivingEvent.Pre event) {
            if (!event.getEntity().getUniqueID().equals(player.getUniqueID())) {
                return;
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(size, size, size);
        }

        @SubscribeEvent
        public void renderEntityPost(RenderLivingEvent.Post event) {
            if (!event.getEntity().getUniqueID().equals(player.getUniqueID())) {
                return;
            }
            GlStateManager.popMatrix();
        }
    }
}
