package com.denizenscript.clientizen.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class UtilPosition {

    public double x;
    public double y;
    public double z;

    public UtilPosition() {
        // Do nothing.
    }

    public UtilPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d toVec3d() {
        return new Vec3d(x, y, z);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x, y, z);
    }
}
