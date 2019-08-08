package net.korti.bettermuffling.common.util;

import net.minecraft.util.math.BlockPos;

public class MathHelper {

    public static boolean isInRange(final BlockPos pos, final BlockPos center, final short range) {
        final float x = ((float)center.getX() + 0.5f) - ((float)pos.getX() + 0.5f);
        final float y = ((float)center.getY() + 0.5f) - ((float)pos.getY() + 0.5f);
        final float z = ((float)center.getZ() + 0.5f) - ((float)pos.getZ() + 0.5f);

        final float distance = (float)Math.sqrt(x * x + y * y + z * z);
        return isInRange(distance, range);
    }

    public static boolean isInRange(final float distance, final short range) {
        return distance <= (range + 1);
    }

}
