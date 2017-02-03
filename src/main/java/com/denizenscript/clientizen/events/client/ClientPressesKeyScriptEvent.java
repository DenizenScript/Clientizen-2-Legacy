package com.denizenscript.clientizen.events.client;

import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientPressesKeyScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // client presses <key> key
    //
    // @Updated 2017/02/02
    //
    // @Cancellable true
    //
    // @Triggers when the client presses the specified keyboard key.
    //
    // @Context
    // None.
    //
    // @Determinations
    // None.
    // -->

    @Override
    public String getName() {
        return "ClientPressesKey";
    }

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("client presses ") && data.eventPath.endsWith(" key");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        for (KeyBinder bind : keysBound) {
            if (bind.eventPath.equals(data.eventPath)) {
                return bind.bind.isPressed();
            }
        }
        return false;
    }

    public InputEvent.KeyInputEvent internal;

    @Override
    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> defs = super.getDefinitions(data);
        return defs;
    }

    public static class KeyBinder {
        public int key;
        public KeyBinding bind;
        public String eventPath;
    }

    public List<KeyBinder> keysBound = new ArrayList<>();

    @Override
    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
        for (ScriptEventData data : usages) {
            String key = data.eventPath.substring("client presses ".length(), data.eventPath.length() - " key".length()).toUpperCase();
            int k = Keyboard.getKeyIndex(key);
            if (k == Keyboard.KEY_NONE) {
                Debug.error("Invalid key: " + key);
            }
            else {
                KeyBinding kb = new KeyBinding("Denizen2_Client_" + k, k, "Denizen2_Client");
                KeyBinder bind = new KeyBinder();
                bind.key = k;
                bind.bind = kb;
                bind.eventPath = data.eventPath;
                keysBound.add(bind);
                Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.add(
                        Minecraft.getMinecraft().gameSettings.keyBindings, kb);
            }
        }
    }

    @Override
    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        for (KeyBinder kb : keysBound) {
            Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.removeElement(
                    Minecraft.getMinecraft().gameSettings.keyBindings, kb.bind);
        }
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent evt) {
        ClientPressesKeyScriptEvent event = (ClientPressesKeyScriptEvent) clone();
        event.internal = evt;
        event.cancelled = false;
        event.run();
    }

    @Override
    public void applyDetermination(boolean errors, String determination, AbstractTagObject value) {
        super.applyDetermination(errors, determination, value);
    }
}
