package io.korti.bettermuffling.client.util;

import com.google.common.collect.ImmutableSet;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;

public final class MufflingCache {

    private static final Map<BlockPos, MufflingBlockEntity> cache = new HashMap<>();

    public static void addMufflingPos(final BlockPos pos, final MufflingBlockEntity tile) {
        cache.put(pos, tile);
    }

    public static void removeMufflingPos(final BlockPos pos) {
        cache.remove(pos);
    }

    public static void reset() {
        cache.clear();
    }

    public static ImmutableSet<Map.Entry<BlockPos, MufflingBlockEntity>> getCache() {
        return ImmutableSet.copyOf(cache.entrySet());
    }

    @EventBusSubscriber(modid = BetterMuffling.MOD_ID)
    public static class ResetHandler {

        @SubscribeEvent
        public static void onPlayerLogOut(final PlayerEvent.PlayerLoggedOutEvent event) {
            MufflingCache.reset();
        }
    }
}
