package com.denizenscript.clientizen.network;

import com.denizenscript.clientizen.Clientizen;
import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.scripts.commontypes.TaskScript;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientizenPluginChannel extends AbstractPluginChannel {

    public ClientizenPluginChannel() {
        super(Clientizen.PLUGIN_CHANNEL);
    }

    @Override
    public void receivePacket(DataDeserializer deserializer) {
        String subchannel = deserializer.readString();
        switch (subchannel) {
            case "ClearAndLoadScripts":
                Map<String, String> scripts = deserializer.readStringMap();
                loadScripts(scripts, true);
                break;
            case "LoadScripts":
                Map<String, String> newScripts = deserializer.readStringMap();
                loadScripts(newScripts, false);
                break;
            case "RemoveScripts":
                List<String> scriptNames = deserializer.readStringList();
                unloadScripts(scriptNames);
                break;
            case "RunScript":
                String scriptName = deserializer.readString();
                Map<String, String> definitions = deserializer.readStringMap();
                runScript(scriptName, definitions);
                break;
            case "RequestConfirmation":
                Clientizen.instance.outputToConsole("Server spotted!");
                sendReady();
                break;
            default:
                Clientizen.instance.outputToConsole("Received unknown packet type: " + subchannel);
                break;
        }
    }

    public void sendReady() {
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("READY");
        sendPacket(serializer);
    }

    private void loadScripts(Map<String, String> scripts, boolean clearExisting) {
        if (clearExisting) {
            Clientizen.instance.remoteScripts.clear();
        }
        for (Map.Entry<String, String> scriptFile : scripts.entrySet()) {
            Clientizen.instance.remoteScripts.put(scriptFile.getKey(), scriptFile.getValue());
        }
        Denizen2Core.reload();
    }

    private void unloadScripts(List<String> scriptNames) {
        for (String script : scriptNames) {
            if (Clientizen.instance.remoteScripts.containsKey(script)) {
                Clientizen.instance.remoteScripts.remove(script);
            }
        }
        Denizen2Core.reload();
    }

    private void runScript(String scriptName, Map<String, String> definitions) {
        CommandScript script = Denizen2Core.currentScripts.get(CoreUtilities.toLowerCase(scriptName));
        if (script == null) {
            Denizen2Core.getImplementation().outputError("The script '" + scriptName + "' does not exist!");
            return;
        }
        if (!(script instanceof TaskScript)) {
            Denizen2Core.getImplementation().outputError("Trying to run a non-task typed script!");
            return;
        }
        TaskScript task = (TaskScript) script;
        CommandScriptSection section = task.getSection("script");
        if (section == null) {
            Denizen2Core.getImplementation().outputError("Invalid script section!");
            return;
        }
        Denizen2Core.getImplementation().outputGood("Running script: " + ColorSet.emphasis + script.title);
        CommandQueue nq = section.toQueue();
        if (definitions.size() > 0) {
            Map<String, AbstractTagObject> defs = new HashMap<>();
            for (Map.Entry<String, String> entry : definitions.entrySet()) {
                defs.put(entry.getKey(), new TextTag(entry.getValue()));
            }
            nq.commandStack.peek().definitions.putAll(defs);
        }
        nq.start();
    }
}
