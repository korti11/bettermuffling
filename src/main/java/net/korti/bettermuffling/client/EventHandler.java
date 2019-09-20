package net.korti.bettermuffling.client;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.client.event.MufflingAreaEvent;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onEnterMufflingArea(final MufflingAreaEvent.PlayerEntered event) {
        final ClientPlayerEntity clientPlayer = event.getClientPlayer();
        final CompoundNBT playerData = clientPlayer.getPersistentData();
        final int mufflingAreas = playerData.getInt("muffling_areas") + 1;
        playerData.putInt("muffling_areas", mufflingAreas);
    }

    @SubscribeEvent
    public static void onLeftMufflingAreas(final MufflingAreaEvent.PlayerLeft event) {
        final ClientPlayerEntity clientPlayer = event.getClientPlayer();
        final CompoundNBT playerData = clientPlayer.getPersistentData();
        final int mufflingAreas = Math.max(0, playerData.getInt("muffling_areas") - 1);
        playerData.putInt("muffling_areas", mufflingAreas);
    }

}
