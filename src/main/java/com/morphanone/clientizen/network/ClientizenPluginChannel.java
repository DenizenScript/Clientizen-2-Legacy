package com.morphanone.clientizen.network;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.scripts.commontypes.TaskScript;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.morphanone.clientizen.Clientizen;
import com.morphanone.clientizen.scripts.ServerScriptManager;

import java.util.HashMap;
import java.util.Map;

public class ClientizenPluginChannel extends AbstractPluginChannel {

    public ClientizenPluginChannel() {
        super(Clientizen.PLUGIN_CHANNEL);
    }

    @Override
    public void receivePacket(DataDeserializer deserializer) {
        Clientizen.instance.outputInfo("Received packet!");
        String subchannel = deserializer.readString();
        if (subchannel.equals("LoadAllScripts")) {
            Clientizen.instance.outputInfo("It's a wild LoadAllScripts!");
            Map<String, String> allScripts = deserializer.readStringMap();
            loadAllScripts(allScripts);
        }
        else if (subchannel.equals("RunScript")) {
            Clientizen.instance.outputInfo("Running script!");
            String scriptName = deserializer.readString();
            Map<String, String> definitions = deserializer.readStringMap();
            runScript(scriptName, definitions);
        }
        else {
            Clientizen.instance.outputInfo("Received unknown packet type: " + subchannel);
        }
    }

    public void sendReady() {
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("READY");
        sendPacket(serializer);
    }

    private void loadAllScripts(Map<String, String> allScripts) {
        ServerScriptManager.clearScripts();
        for (Map.Entry<String, String> scriptFile : allScripts.entrySet()) {
            ServerScriptManager.addScript(scriptFile.getKey(), scriptFile.getValue());
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
        if (definitions.size() > 1) {
            Map<String, AbstractTagObject> defs = new HashMap<>();
            for (Map.Entry<String, String> entry : definitions.entrySet()) {
                defs.put(entry.getKey(), new TextTag(entry.getValue()));
            }
            nq.commandStack.peek().definitions.putAll(defs);
        }
        nq.start();
    }
}
