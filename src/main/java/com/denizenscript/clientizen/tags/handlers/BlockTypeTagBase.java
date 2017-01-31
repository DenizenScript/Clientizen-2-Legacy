package com.denizenscript.clientizen.tags.handlers;

import com.denizenscript.clientizen.tags.objects.BlockTypeTag;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;

public class BlockTypeTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base block_type[<BlockTypeTag>]
    // @Group Forge Base Types
    // @ReturnType BlockTypeTag
    // @Returns the input as a BlockTypeTag.
    // -->

    @Override
    public String getName() {
        return "block_type";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return BlockTypeTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
