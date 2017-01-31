package com.denizenscript.clientizen.tags.handlers;

import com.denizenscript.clientizen.tags.objects.PositionTag;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;

public class PositionTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base position[<PositionTag>]
    // @Group Forge Base Types
    // @ReturnType PositionTag
    // @Returns the input as a PositionTag.
    // -->

    @Override
    public String getName() {
        return "position";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return PositionTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
