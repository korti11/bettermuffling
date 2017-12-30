package net.korti.bettermuffling.common;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.client.gui.GuiHandler;
import net.korti.bettermuffling.common.network.PacketNetworkHandler;
import net.korti.bettermuffling.common.registry.TileEntities;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit() {
        TileEntities.registerTileEntities();
    }

    public void init() {
        PacketNetworkHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(BetterMuffling.instance, new GuiHandler());
    }
}
