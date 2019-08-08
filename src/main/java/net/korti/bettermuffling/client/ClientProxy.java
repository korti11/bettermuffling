package net.korti.bettermuffling.client;

import net.korti.bettermuffling.common.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

public class ClientProxy extends ServerProxy {

    public static ClientWorld getWorld() {
        return Minecraft.getInstance().world;
    }

}
