package net.korti.bettermuffling.common.network.packet;

import net.korti.bettermuffling.client.event.MufflingAreaEvent;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MufflingAreaEventPacket {

    public static final MufflingAreaEventPacket PLAYER_ENTERED = new MufflingAreaEventPacket(EventType.PLAYER_ENTERED);

    public static final MufflingAreaEventPacket PLAYER_LEFT = new MufflingAreaEventPacket(EventType.PLAYER_LEFT);

    private final EventType type;

    private MufflingAreaEventPacket(final EventType type) {
        this.type = type;
    }

    public static void encoder(final MufflingAreaEventPacket packet, final PacketBuffer buf) {
        buf.writeEnumValue(packet.type);
    }

    public static MufflingAreaEventPacket decode(final PacketBuffer buf) {
        return new MufflingAreaEventPacket(buf.readEnumValue(EventType.class));
    }

    private enum EventType {
        PLAYER_ENTERED,
        PLAYER_LEFT
    }

    public static class Handler {
        public static void handle(final MufflingAreaEventPacket packet, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                final EventType type = packet.type;
                switch (type) {
                    case PLAYER_ENTERED:
                        MinecraftForge.EVENT_BUS.post(new MufflingAreaEvent.PlayerEntered());
                        break;
                    case PLAYER_LEFT:
                        MinecraftForge.EVENT_BUS.post(new MufflingAreaEvent.PlayerLeft());
                        break;
                    default:
                        System.err.println("Given event type is not supported: " + type.name());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
