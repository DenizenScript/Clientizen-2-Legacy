package com.morphanone.clientizen;

import com.denizenscript.denizen2core.utilities.CoreUtilities;

public class Settings {

    public static boolean enforceLocale() {
        return CoreUtilities.toLowerCase(Clientizen.instance.config.getString("Enforce Locale", "true")).equals("true");
    }

    public static boolean debugGeneral() {
        return CoreUtilities.toLowerCase(Clientizen.instance.config.getString("Debug.General", "true")).equals("true");
    }
}
