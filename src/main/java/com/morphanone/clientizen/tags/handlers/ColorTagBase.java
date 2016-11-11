package com.morphanone.clientizen.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.morphanone.clientizen.tags.objects.ColorTag;

public class ColorTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base color[<ColorTag>]
    // @Group Forge Base Types
    // @ReturnType ColorTag
    // @Returns the input as a ColorTag.
    // -->

    @Override
    public String getName() {
        return "color";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return ColorTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
