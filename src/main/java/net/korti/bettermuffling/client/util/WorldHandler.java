package net.korti.bettermuffling.client.util;

import net.korti.bettermuffling.BetterMuffling;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID)
public final class WorldHandler {

    @SubscribeEvent
    public void onUnloadWorld(final WorldEvent.Unload event) {
        MufflingCache.reset();
    }

}
