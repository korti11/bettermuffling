package io.korti.bettermuffling.client;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.event.MufflingAreaEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BetterMuffling.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onEnterMufflingArea(final MufflingAreaEvent.PlayerEntered event) {
        final LocalPlayer clientPlayer = event.getClientPlayer();
        final CompoundTag playerData = clientPlayer.getPersistentData();
        final int mufflingAreas = playerData.getInt("muffling_areas") + 1;
        playerData.putInt("muffling_areas", mufflingAreas);
    }

    @SubscribeEvent
    public static void onLeftMufflingAreas(final MufflingAreaEvent.PlayerLeft event) {
        final LocalPlayer clientPlayer = event.getClientPlayer();
        final CompoundTag playerData = clientPlayer.getPersistentData();
        final int mufflingAreas = Math.max(0, playerData.getInt("muffling_areas") - 1);
        playerData.putInt("muffling_areas", mufflingAreas);
    }

}
