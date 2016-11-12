package com.morphanone.clientizen.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.Function2;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorTag extends AbstractTagObject {

    // <--[object]
    // @Type ColorTag
    // @SubType TextTag
    // @Group Forge Tags
    // @Description Represents a color in RGB format.
    // -->

    private Color internal;

    public ColorTag(Color internal) {
        this.internal = internal;
    }

    public Color getInternal() {
        return internal;
    }

    public static final HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    private static final Map<String, Color> NAMED_COLORS = new HashMap<>();

    static {
        // Set named colors
        for (Field field : Color.class.getFields()) {
            if (!field.getDeclaringClass().equals(Color.class)) {
                continue;
            }
            String name = field.getName();
            if (!CoreUtilities.toUpperCase(name).equals(name)) {
                // Only get UPPER_CASE field names.
                continue;
            }
            try {
                NAMED_COLORS.put(name, (Color) field.get(null));
            }
            catch (IllegalAccessException e) {
                // All the fields are public
            }
        }

        // <--[tag]
        // @Name ColorTag.red
        // @Updated 2016/11/04
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the red value of the color.
        // @Example "255,128,0" .red returns "255".
        // -->
        handlers.put("red", (dat, obj) -> new IntegerTag(((ColorTag) obj).internal.getRed()));

        // <--[tag]
        // @Name ColorTag.green
        // @Updated 2016/11/04
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the green value of the color.
        // @Example "255,128,0" .red returns "128".
        // -->
        handlers.put("green", (dat, obj) -> new IntegerTag(((ColorTag) obj).internal.getGreen()));

        // <--[tag]
        // @Name ColorTag.blue
        // @Updated 2016/11/04
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the blue value of the color.
        // @Example "255,128,0" .red returns "0".
        // -->
        handlers.put("blue", (dat, obj) -> new IntegerTag(((ColorTag) obj).internal.getBlue()));
    }

    public static ColorTag getFor(Action<String> error, String text) {
        text = CoreUtilities.toUpperCase(text);
        if (NAMED_COLORS.containsKey(text)) {
            return new ColorTag(NAMED_COLORS.get(text));
        }
        else if (text.indexOf(',') == -1) {
            StringBuilder sb = new StringBuilder();
            for (String name : NAMED_COLORS.keySet()) {
                sb.append(", ").append(name);
            }
            error.run("Invalid named color! Valid: " + sb.substring(2));
            return null;
        }
        List<String> split = CoreUtilities.split(text, ',');
        if (split.size() != 3) {
            error.run("RGB color must have exactly 3 values: RED,GREEN,BLUE.");
            return null;
        }
        IntegerTag rTag = IntegerTag.getFor(error, split.get(0));
        IntegerTag gTag = IntegerTag.getFor(error, split.get(1));
        IntegerTag bTag = IntegerTag.getFor(error, split.get(2));
        return new ColorTag(new Color(rTag.getInternal(), gTag.getInternal(), bTag.getInternal()));
    }

    public static ColorTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof ColorTag) ? (ColorTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new TextTag(toString()).handle(data);
    }

    @Override
    public String toString() {
        return internal.getRed() + "," + internal.getGreen() + "," + internal.getBlue();
    }
}
