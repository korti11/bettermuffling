package net.korti.bettermuffling.client.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public final class MufflingCache {

    private static Map<BlockPos, Short> cache = new HashMap<>();

    public static void addMufflingPos(final BlockPos pos, final short range) {
        cache.put(pos, range);
    }

    public static void removeMufflingPos(final BlockPos pos) {
        cache.remove(pos);
    }

    public static void reset() {
        cache.clear();
    }

    public static ImmutableSet<Map.Entry<BlockPos, Short>> getCache() {
        return ImmutableSet.copyOf(cache.entrySet());
    }

}
