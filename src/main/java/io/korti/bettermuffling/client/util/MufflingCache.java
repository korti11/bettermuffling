package io.korti.bettermuffling.client.util;

import com.google.common.collect.ImmutableSet;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

public final class MufflingCache {

    private static final Map<BlockPos, TileMuffling> cache = new HashMap<>();

    public static void addMufflingPos(final BlockPos pos, final TileMuffling tile) {
        cache.put(pos, tile);
    }

    public static void removeMufflingPos(final BlockPos pos) {
        cache.remove(pos);
    }

    public static void reset() {
        cache.clear();
    }

    public static ImmutableSet<Map.Entry<BlockPos, TileMuffling>> getCache() {
        return ImmutableSet.copyOf(cache.entrySet());
    }

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ResetHandler {

        @SubscribeEvent
        public static void onPlayerLogOut(final PlayerEvent.PlayerLoggedOutEvent event) {
            MufflingCache.reset();
        }

    }

}
