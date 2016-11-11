package com.morphanone.clientizen.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.Function2;

import java.util.HashMap;

public class ClientTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base client
    // @Group Utilities
    // @ReturnType ClientTag
    // @Returns a generic utility class full of specific helpful client-related tags.
    // -->

    @Override
    public String getName() {
        return "client";
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name ClientTag.screen_width
        // @Updated 2016/11/10
        // @Group Utilities
        // @ReturnType IntegerTag
        // @Returns the width of the client's current screen.
        // -->
        //handlers.put("screen_width", (dat, obj) -> new IntegerTag(Minecraft.getMinecraft().currentScreen.width));
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return new ClientTag().handle(data.shrink());
    }

    public class ClientTag extends AbstractTagObject {

        @Override
        public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
            return handlers;
        }

        @Override
        public AbstractTagObject handleElseCase(TagData data) {
            return new TextTag(getName()).handle(data);
        }
    }
}
