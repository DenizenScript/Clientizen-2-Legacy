package com.denizenscript.clientizen.tags.handlers;

import com.denizenscript.clientizen.tags.objects.PositionTag;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.Function2;
import net.minecraft.client.Minecraft;

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
        // @Name ClientTag.display_width
        // @Updated 2017/01/26
        // @Group Utilities
        // @ReturnType IntegerTag
        // @Returns the width of the client's display.
        // -->
        handlers.put("display_width", (dat, obj) -> new IntegerTag(Minecraft.getMinecraft().displayWidth));
        // <--[tag]
        // @Name ClientTag.display_height
        // @Updated 2017/01/26
        // @Group Utilities
        // @ReturnType IntegerTag
        // @Returns the width of the client's height.
        // -->
        handlers.put("display_height", (dat, obj) -> new IntegerTag(Minecraft.getMinecraft().displayHeight));
        // <--[tag]
        // @Name ClientTag.position
        // @Updated 2017/01/30
        // @Group General Information
        // @ReturnType PositionTag
        // @Returns the position the client is currently spawned at (0,0,0 when not in a world).
        // -->
        handlers.put("position", (dat, obj) -> new PositionTag(Minecraft.getMinecraft().player.getPositionVector()));
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
