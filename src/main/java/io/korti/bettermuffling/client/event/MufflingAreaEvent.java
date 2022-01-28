package io.korti.bettermuffling.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.eventbus.api.Event;

public abstract class MufflingAreaEvent extends Event {

    private final LocalPlayer clientPlayer = Minecraft.getInstance().player;

    public LocalPlayer getClientPlayer() {
        return clientPlayer;
    }

    public static class PlayerEntered extends MufflingAreaEvent { }

    public static class PlayerLeft extends MufflingAreaEvent { }
}
