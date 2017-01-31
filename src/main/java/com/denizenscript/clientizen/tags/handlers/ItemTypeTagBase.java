package com.denizenscript.clientizen.tags.handlers;

import com.denizenscript.clientizen.tags.objects.ItemTypeTag;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;

public class ItemTypeTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base item_type[<ItemTypeTag>]
    // @Group Forge Base Types
    // @ReturnType ItemTypeTag
    // @Returns the input as a ItemTypeTag.
    // -->

    @Override
    public String getName() {
        return "item_type";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return ItemTypeTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
