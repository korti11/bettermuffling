package net.korti.bettermuffling.common.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;
import java.util.Set;

public class TileCache {

    private static Set<TileEntity> cache = new HashSet<>();

    public static void addTileEntity(TileEntity tile) {
        cache.add(tile);
    }

    public static void removeTileEntity(TileEntity tile) {
        cache.remove(tile);
    }

    public static ImmutableSet<TileEntity> getCache() {
        return ImmutableSet.copyOf(cache);
    }

}
