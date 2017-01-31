package com.denizenscript.clientizen.events.client;

import com.denizenscript.clientizen.tags.objects.PositionTag;
import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class ClientRightClicksBlockScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // client right clicks block
    //
    // @Updated 2017/01/30
    //
    // @Cancellable true
    //
    // @Triggers when this client right clicks a block. Note that this may fire twice per triggering.
    //
    // @Context
    // position (PositionTag) returns the position of the block that was right clicked.
    // intersection (PositionTag) returns the exact point of intersection on the block where it was clicked.
    //
    // @Determinations
    // None.
    // -->

    @Override
    public String getName() {
        return "ClientRightClicksBlock";
    }

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("client right clicks block");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return true;
    }

    public PositionTag position;

    public PositionTag intersection;

    public PlayerInteractEvent.RightClickBlock internal;

    @Override
    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> defs = super.getDefinitions(data);
        defs.put("position", position);
        defs.put("intersection", intersection);
        return defs;
    }

    @Override
    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock evt) {
        if (!evt.getEntityPlayer().isUser()) {
            return;
        }
        ClientRightClicksBlockScriptEvent event = (ClientRightClicksBlockScriptEvent) clone();
        event.internal = evt;
        event.position = new PositionTag(evt.getPos());
        event.intersection = new PositionTag(evt.getHitVec());
        event.cancelled = evt.isCanceled();
        event.run();
        evt.setCanceled(event.cancelled);
    }

    @Override
    public void applyDetermination(boolean errors, String determination, AbstractTagObject value) {
        super.applyDetermination(errors, determination, value);
    }
}
