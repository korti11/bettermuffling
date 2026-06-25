package io.korti.bettermuffling.common.network;

import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import io.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import io.korti.bettermuffling.common.network.packet.RequestMufflingUpdatePacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class PacketHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                OpenScreenPacket.TYPE,
                OpenScreenPacket.STREAM_CODEC,
                OpenScreenPacket::handle);

        registrar.playToServer(
                RequestMufflingUpdatePacket.TYPE,
                RequestMufflingUpdatePacket.STREAM_CODEC,
                RequestMufflingUpdatePacket::handle);

        registrar.playBidirectional(
                MufflingDataPacket.TYPE,
                MufflingDataPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(MufflingDataPacket::handleClient, MufflingDataPacket::handleServer));
    }
}
