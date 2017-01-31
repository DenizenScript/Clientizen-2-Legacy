package com.denizenscript.clientizen.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.Function2;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;

public class BlockTypeTag extends AbstractTagObject {

    // <--[object]
    // @Type BlockTypeTag
    // @SubType TextTag
    // @Group Items
    // @Description Represents a block type. Identified by block type ID.
    // -->

    private Block internal;

    public BlockTypeTag(Block internal) {
        this.internal = internal;
    }

    public Block getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name BlockTypeTag.id
        // @Updated 2017/01/31
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the ID of the item type.
        // @Example "minecraft:dirt" .id returns "minecraft:dirt".
        // -->
        handlers.put("id", (dat, obj) -> new TextTag(((BlockTypeTag) obj).internal.getRegistryName().toString()));
        // <--[tag]
        // @Name BlockTypeTag.item_type
        // @Updated 2017/01/31
        // @Group Identification
        // @ReturnType ItemTypeTag
        // @Returns the item type equivalent of this block type.
        // -->
        handlers.put("item_type", (dat, obj) -> new ItemTypeTag(Item.getItemFromBlock(((BlockTypeTag) obj).internal)));
        // <--[tag]
        // @Name BlockTypeTag.name
        // @Updated 2017/01/31
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the name of the item type.
        // @Example "minecraft:dirt" .name returns "dirt".
        // -->
        handlers.put("name", (dat, obj) -> new TextTag(CoreUtilities.after(((BlockTypeTag) obj).internal.getRegistryName().getResourcePath(), ":")));
    }

    public static BlockTypeTag getFor(Action<String> error, String text) {
        List<String> split = CoreUtilities.split(CoreUtilities.toLowerCase(text), ':', 2);
        boolean hasDomain = split.size() > 1;
        String domain = hasDomain ? split.get(0) : "";
        String path = hasDomain ? split.get(1) : split.get(0);
        for (ResourceLocation location : Block.REGISTRY.getKeys()) {
            if ((hasDomain && location.getResourceDomain().equals(domain)) && location.getResourcePath().equals(path)) {
                return new BlockTypeTag(Block.REGISTRY.getObject(location));
            }
        }
        error.run("Invalid BlockTypeTag input!");
        return null;
    }

    public static BlockTypeTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof BlockTypeTag) ? (BlockTypeTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new TextTag(toString());
    }

    @Override
    public String toString() {
        return internal.getRegistryName().toString();
    }
}
