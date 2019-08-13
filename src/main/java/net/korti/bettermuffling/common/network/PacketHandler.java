package net.korti.bettermuffling.common.network;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler {

    private static final String PROTOCOL_VERSION = Integer.toString(1);

    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(BetterMuffling.MOD_ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        int id = 0;

        HANDLER.registerMessage(id, OpenScreenPacket.class, OpenScreenPacket::encode, OpenScreenPacket::decode,
                OpenScreenPacket.Handler::handle);
    }

    public static <MSG> void sendToPlayer(final PacketDistributor.PacketTarget target, MSG message) {
        HANDLER.send(target, message);
    }
}
