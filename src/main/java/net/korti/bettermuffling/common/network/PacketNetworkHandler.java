package net.korti.bettermuffling.common.network;

import net.korti.bettermuffling.common.constant.ModInfo;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketNetworkHandler {

    private static final SimpleNetworkWrapper NETWORK_WRAPPER = new SimpleNetworkWrapper(ModInfo.MOD_ID);
    private static int id = 0;

    public static void init() {
        NETWORK_WRAPPER.registerMessage(UpdateTileEntityMessage.MessageHandler.class,
                UpdateTileEntityMessage.class, id++, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(UpdateTileEntityRequestMessage.MessageHandler.class,
                UpdateTileEntityRequestMessage.class, id++, Side.SERVER);
    }

    public static void sendToServer(IMessage message) {
        NETWORK_WRAPPER.sendToServer(message);
    }

}
