package com.denizenscript.clientizen.commands.entity;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.NumberTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.UUID;

public class ScaleEntityCommand extends AbstractCommand {

    // <--[command]
    // @Name scaleentity
    // @Arguments <size>/<x-size>,<y-size>,<z-size> [uuid]
    // @Short rescales the local player, or a specified entity.
    // @Updated 2016/12/16
    // @Group Entity
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Rescales the local player, or a specified entity.
    // @Example
    // # This example scales the local player to half size.
    // - scaleplayer 0.5
    // -->

    @Override
    public String getName() {
        return "scaleentity";
    }

    @Override
    public String getArguments() {
        return "<size>/<x-size>,<y-size>,<z-size> [uuid]";
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
        ScalePlayerSystem scs = new ScalePlayerSystem();
        AbstractTagObject ato =  entry.getArgumentObject(queue, 0);
        String atostr = ato.toString();
        // TODO: Vector/Location tag?
        if (atostr.contains(",")) {
            String[] split = atostr.split(",");
            scs.sizeX = (float) NumberTag.getFor(queue.error, split[0]).getInternal();
            scs.sizeY = (float) NumberTag.getFor(queue.error, split[1]).getInternal();
            scs.sizeZ = (float) NumberTag.getFor(queue.error, split[2]).getInternal();
        }
        else {
            float t = (float) NumberTag.getFor(queue.error, ato).getInternal();
            scs.sizeX = t;
            scs.sizeY = t;
            scs.sizeZ = t;
        }
        UUID uuid;
        if (entry.arguments.size() > 1) {
            uuid = UUID.fromString(entry.getArgumentObject(queue, 1).toString());
        }
        else {
            uuid = Minecraft.getMinecraft().thePlayer.getUniqueID();
        }
        if (mainScs.containsKey(uuid)) {
            ScalePlayerSystem tscs = mainScs.get(uuid);
            if (tscs.player != null) {
                tscs.player.eyeHeight /= tscs.sizeY;
            }
            tscs.disable();
            mainScs.remove(uuid);
        }
        if (scs.sizeY == 1.0f && scs.sizeX == 1.0f && scs.sizeZ == 1.0f) {
            if (queue.shouldShowGood()) {
                queue.outGood("Ignoring a non-editing scaler for: " + uuid);
            }
            return;
        }
        scs.affectOnly = uuid;
        if (uuid.equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
            scs.player = Minecraft.getMinecraft().thePlayer;
        }
        if (scs.player != null) {
            scs.player.eyeHeight *= scs.sizeY;
            scs.eyeHeight = scs.player.eyeHeight;
        }
        scs.register();
        mainScs.put(uuid, scs);
        if (queue.shouldShowGood()) {
            queue.outGood("Registered a scaler for: " + uuid);
        }
    }

    public static HashMap<UUID, ScalePlayerSystem> mainScs = new HashMap<>();

    public class ScalePlayerSystem {

        public float sizeX;

        public float sizeY;

        public float sizeZ;

        public float eyeHeight;

        public EntityPlayer player;

        public UUID affectOnly;

        public void register() {
            MinecraftForge.EVENT_BUS.register(this);
        }

        public void disable() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }

        @SubscribeEvent
        public void onCamera(EntityViewRenderEvent.CameraSetup event) {
            if (player != null) {
                GlStateManager.scale(1.0f / sizeY, 1.0f / sizeY, 1.0 / sizeY);
                player.eyeHeight = eyeHeight;
            }
        }

        @SubscribeEvent
        public void renderEntityPre(RenderLivingEvent.Pre event) {
            if (!event.getEntity().getUniqueID().equals(affectOnly)) {
                return;
            }
            GlStateManager.pushMatrix();
            if (player == null) {
                GlStateManager.translate(-event.getX() * sizeX + event.getX(), -event.getY() * sizeY + event.getY(), -event.getZ() * sizeZ + event.getZ());
            }
            GlStateManager.scale(sizeX, sizeY, sizeZ);
            GlStateManager.pushMatrix();
        }

        @SubscribeEvent
        public void renderEntityPost(RenderLivingEvent.Post event) {
            if (!event.getEntity().getUniqueID().equals(affectOnly)) {
                return;
            }
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }
}
