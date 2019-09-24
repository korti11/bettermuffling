package io.korti.bettermuffling.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public abstract class MufflingAreaEvent extends Event {

    private final ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;

    public ClientPlayerEntity getClientPlayer() {
        return clientPlayer;
    }

    public static class PlayerEntered extends MufflingAreaEvent { }

    public static class PlayerLeft extends MufflingAreaEvent { }
}
