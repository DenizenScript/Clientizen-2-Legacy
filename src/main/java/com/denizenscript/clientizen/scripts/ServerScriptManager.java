package com.denizenscript.clientizen.scripts;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.clientizen.Clientizen;

import java.util.HashMap;
import java.util.Map;

public class ServerScriptManager {

    private static final Map<String, String> scripts = new HashMap<>();

    public static void addScript(String fileName, String contents) {
        Clientizen.instance.outputToConsole("Adding scripts: " + fileName + "\r\nContents:\r\n");
        Clientizen.instance.outputToConsole(contents);
        scripts.put(fileName, contents);
    }

    public static void injectScripts() {
        Clientizen.instance.outputToConsole("Injecting scripts!");
        ScriptEvent.currentWorldScripts.clear();
        for (Map.Entry<String, String> scriptFile : scripts.entrySet()) {
            Clientizen.instance.outputToConsole("Loading: " + scriptFile.getKey());
            Denizen2Core.loadFile(scriptFile.getKey() + " (Sent by server)", scriptFile.getValue());
        }
        for (ScriptEvent event : Denizen2Core.events) {
            event.init();
        }
        Clientizen.instance.outputToConsole("All scripts:");
        for (String script : Denizen2Core.currentScripts.keySet()) {
            Clientizen.instance.outputToConsole(script);
        }
    }

    public static void clearScripts() {
        Clientizen.instance.outputToConsole("Clearing scripts!");
        scripts.clear();
        Denizen2Core.reload();
    }
}
