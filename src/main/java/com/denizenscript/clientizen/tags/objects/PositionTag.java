package com.denizenscript.clientizen.tags.objects;

import com.denizenscript.clientizen.util.UtilPosition;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.tags.objects.NumberTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.Function2;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.HashMap;
import java.util.List;

public class PositionTag extends AbstractTagObject {

    // <--[object]
    // @Type PositionTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a position or vector. Identified in the format "x,y,z".
    // -->

    private UtilPosition internal = new UtilPosition();

    public PositionTag(UtilPosition position) {
        this(position.x, position.y, position.z);
    }

    public PositionTag(Vec3i position) {
        this(position.getX(), position.getY(), position.getZ());
    }

    public PositionTag(Vec3d position) {
        this(position.x, position.y, position.z);
    }

    public PositionTag(double x, double y, double z) {
        this.internal.x = x;
        this.internal.y = y;
        this.internal.z = z;
    }

    public UtilPosition getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name PositionTag.x
        // @Updated 2017/01/30
        // @Group Identification
        // @ReturnType NumberTag
        // @Returns the X coordinate of the position.
        // @Example "0,1,2,world" .x returns "0".
        // -->
        handlers.put("x", (dat, obj) -> new NumberTag(((PositionTag) obj).internal.x));
        // <--[tag]
        // @Name PositionTag.y
        // @Updated 2017/01/30
        // @Group Identification
        // @ReturnType NumberTag
        // @Returns the Y coordinate of the position.
        // @Example "0,1,2,world" .y returns "1".
        // -->
        handlers.put("y", (dat, obj) -> new NumberTag(((PositionTag) obj).internal.y));
        // <--[tag]
        // @Name PositionTag.z
        // @Updated 2017/01/30
        // @Group Identification
        // @ReturnType NumberTag
        // @Returns the Z coordinate of the position.
        // @Example "0,1,2,world" .z returns "2".
        // -->
        handlers.put("z", (dat, obj) -> new NumberTag(((PositionTag) obj).internal.z));
        // <--[tag]
        // @Name PositionTag.add[<PositionTag>]
        // @Updated 2017/01/30
        // @Group Mathematics
        // @ReturnType PositionTag
        // @Returns the position with the specified position vector added to it.
        // -->
        handlers.put("add", (dat, obj) -> {
            UtilPosition t = ((PositionTag) obj).internal;
            UtilPosition a = PositionTag.getFor(dat.error, dat.getNextModifier()).getInternal();
            return new PositionTag(t.x + a.x, t.y + a.y, t.z + a.z);
        });
        // <--[tag]
        // @Name PositionTag.block_type
        // @Updated 2017/01/31
        // @Group General Information
        // @ReturnType BlockTypeTag
        // @Returns the type of the block at the position.
        // -->
        handlers.put("block_type", (dat, obj) -> new BlockTypeTag(Minecraft.getMinecraft().world
                .getBlockState(((PositionTag) obj).internal.toBlockPos()).getBlock()));
        // <--[tag]
        // @Name PositionTag.door_hinge
        // @Updated 2017/02/01
        // @Group General Information
        // @ReturnType TextTag
        // @Returns the side of the door hinge (either 'left' or 'right').
        // -->
        handlers.put("door_hinge", (dat, obj) -> {
            IBlockState state = Minecraft.getMinecraft().world.getBlockState(((PositionTag) obj).getInternal().toBlockPos());
            if (!(state.getBlock() instanceof BlockDoor)) {
                dat.error.run("Block at position is not a door!");
                return new NullTag();
            }
            return new TextTag(state.getValue(BlockDoor.HINGE).getName());
        });
        // <--[tag]
        // @Name PositionTag.door_facing
        // @Updated 2017/02/01
        // @Group General Information
        // @ReturnType TextTag
        // @Returns the direction the door is facing ('north', 'south', 'west', or 'east').
        // -->
        handlers.put("door_facing", (dat, obj) -> {
            IBlockState state = Minecraft.getMinecraft().world.getBlockState(((PositionTag) obj).getInternal().toBlockPos());
            if (!(state.getBlock() instanceof BlockDoor)) {
                dat.error.run("Block at position is not a door!");
                return new NullTag();
            }
            return new TextTag(state.getValue(BlockDoor.FACING).getName());
        });
    }

    public static PositionTag getFor(Action<String> error, String text) {
        List<String> split = CoreUtilities.split(text, ',', 3);
        NumberTag x = NumberTag.getFor(error, split.get(0));
        NumberTag y = NumberTag.getFor(error, split.get(1));
        NumberTag z = NumberTag.getFor(error, split.get(2));
        return new PositionTag(x.getInternal(), y.getInternal(), z.getInternal());
    }

    public static PositionTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof PositionTag) ? (PositionTag) text : getFor(error, text.toString());
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
        return "PositionTag";
    }

    @Override
    public String toString() {
        return CoreUtilities.doubleToString(internal.x) + ","
                + CoreUtilities.doubleToString(internal.y) + ","
                + CoreUtilities.doubleToString(internal.z);
    }
}
