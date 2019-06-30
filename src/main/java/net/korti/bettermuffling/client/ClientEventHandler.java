package net.korti.bettermuffling.client;

import net.korti.bettermuffling.client.event.MufflingAreaEvent;
import net.korti.bettermuffling.client.event.SoundLevelUpdateEvent;
import net.korti.bettermuffling.common.util.TileCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Set;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        final Set<TileEntity> entities = TileCache.getCache();
        for (TileEntity tile : entities) {
            MinecraftForge.EVENT_BUS.unregister(tile);
            TileCache.removeTileEntity(tile);
        }
    }

    @SubscribeEvent
    public static void onEnterMufflingArea(MufflingAreaEvent.PlayerEntered event) {
        final EntityPlayer player = event.getClientPlayer();
        final NBTTagCompound playerData = player.getEntityData();
        final int mufflingAreas = playerData.getInteger("muffling_areas") + 1;
        playerData.setInteger("muffling_areas", mufflingAreas);
    }

    @SubscribeEvent
    public static void onLeftMufflingArea(MufflingAreaEvent.PlayerLeft event) {
        final EntityPlayer player = event.getClientPlayer();
        final NBTTagCompound playerData = player.getEntityData();
        final int mufflingAreas = Math.max(0, playerData.getInteger("muffling_areas") - 1);
        playerData.setInteger("muffling_areas", mufflingAreas);
    }
}
