package io.korti.bettermuffling.common.network;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import io.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import io.korti.bettermuffling.common.network.packet.RequestMufflingUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

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

        HANDLER.registerMessage(id++, OpenScreenPacket.class, OpenScreenPacket::encode, OpenScreenPacket::decode,
                OpenScreenPacket.Handler::handle);
        HANDLER.registerMessage(id++, RequestMufflingUpdatePacket.class, RequestMufflingUpdatePacket::encode,
                RequestMufflingUpdatePacket::decode, RequestMufflingUpdatePacket.Handler::handle);
        HANDLER.registerMessage(id++, MufflingDataPacket.class, MufflingDataPacket::encode, MufflingDataPacket::decode,
                MufflingDataPacket.Handler::handle);
    }

    public static <MSG> void send(final PacketDistributor.PacketTarget target, MSG message) {
        HANDLER.send(target, message);
    }
}
