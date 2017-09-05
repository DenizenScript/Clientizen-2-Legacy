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

public class ItemTypeTag extends AbstractTagObject {

    // <--[object]
    // @Type ItemTypeTag
    // @SubType TextTag
    // @Group Items
    // @Description Represents an item type. Identified by item type ID.
    // -->

    private Item internal;

    public ItemTypeTag(Item internal) {
        this.internal = internal;
    }

    public Item getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name ItemTypeTag.block_type
        // @Updated 2017/01/31
        // @Group Identification
        // @ReturnType BlockTypeTag
        // @Returns the block type equivalent of this item type.
        // -->
        handlers.put("block_type", (dat, obj) -> new BlockTypeTag(Block.getBlockFromItem(((ItemTypeTag) obj).internal)));
        // <--[tag]
        // @Name ItemTypeTag.id
        // @Updated 2017/01/31
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the ID of the item type.
        // @Example "minecraft:dirt" .id returns "minecraft:dirt".
        // -->
        handlers.put("id", (dat, obj) -> new TextTag(((ItemTypeTag) obj).internal.getRegistryName().toString()));
        // <--[tag]
        // @Name ItemTypeTag.name
        // @Updated 2017/01/31
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the name of the item type.
        // @Example "minecraft:dirt" .name returns "dirt".
        // -->
        handlers.put("name", (dat, obj) -> new TextTag(((ItemTypeTag) obj).internal.getRegistryName().getResourcePath()));
    }

    public static ItemTypeTag getFor(Action<String> error, String text) {
        List<String> split = CoreUtilities.split(CoreUtilities.toLowerCase(text), ':', 2);
        boolean hasDomain = split.size() > 1;
        String domain = hasDomain ? split.get(0) : "";
        String path = hasDomain ? split.get(1) : split.get(0);
        for (ResourceLocation location : Item.REGISTRY.getKeys()) {
            if ((!hasDomain || location.getResourceDomain().equals(domain)) && location.getResourcePath().equals(path)) {
                return new ItemTypeTag(Item.REGISTRY.getObject(location));
            }
        }
        error.run("Invalid ItemTypeTag input!");
        return null;
    }

    public static ItemTypeTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof ItemTypeTag) ? (ItemTypeTag) text : getFor(error, text.toString());
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
    public String getTagTypeName() {
        return "ItemTypeTag";
    }

    @Override
    public String toString() {
        return internal.getRegistryName().toString();
    }
}
