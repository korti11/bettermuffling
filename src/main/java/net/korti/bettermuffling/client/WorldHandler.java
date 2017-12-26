package net.korti.bettermuffling.client;

import net.korti.bettermuffling.common.util.TileCache;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Set;

@Mod.EventBusSubscriber(Side.CLIENT)
public class WorldHandler {

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        final Set<TileEntity> entities = TileCache.getCache();
        for (TileEntity tile : entities) {
            MinecraftForge.EVENT_BUS.unregister(tile);
            TileCache.removeTileEntity(tile);
        }
    }

}
