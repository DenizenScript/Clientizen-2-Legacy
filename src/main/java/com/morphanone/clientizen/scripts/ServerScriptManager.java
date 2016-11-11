package com.morphanone.clientizen.scripts;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.events.ScriptEvent;
import com.morphanone.clientizen.Clientizen;

import java.util.HashMap;
import java.util.Map;

public class ServerScriptManager {

    private static final Map<String, String> scripts = new HashMap<>();

    public static void addScript(String fileName, String contents) {
        Clientizen.instance.outputInfo("Adding scripts: " + fileName + "\r\nContents:\r\n");
        Clientizen.instance.outputInfo(contents);
        scripts.put(fileName, contents);
    }

    public static void injectScripts() {
        Clientizen.instance.outputInfo("Injecting scripts!");
        ScriptEvent.currentWorldScripts.clear();
        for (Map.Entry<String, String> scriptFile : scripts.entrySet()) {
            Clientizen.instance.outputInfo("Loading: " + scriptFile.getKey());
            Denizen2Core.loadFile(scriptFile.getKey() + " (Sent by server)", scriptFile.getValue());
        }
        for (ScriptEvent event : Denizen2Core.events) {
            event.init();
        }
        Clientizen.instance.outputInfo("All scripts:");
        for (String script : Denizen2Core.currentScripts.keySet()) {
            Clientizen.instance.outputInfo(script);
        }
    }

    public static void clearScripts() {
        Clientizen.instance.outputInfo("Clearing scripts!");
        scripts.clear();
        Denizen2Core.reload();
    }
}
