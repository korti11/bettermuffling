package net.korti.bettermuffling.client.event;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MufflingAreaEvent extends Event {

    private final EntityPlayerSP clientPlayer = FMLClientHandler.instance().getClientPlayerEntity();

    private MufflingAreaEvent() {
    }

    public EntityPlayerSP getClientPlayer() {
        return clientPlayer;
    }

    public static class PlayerEntered extends MufflingAreaEvent { }

    public static class PlayerLeft extends MufflingAreaEvent { }

}
