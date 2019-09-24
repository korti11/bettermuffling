package io.korti.bettermuffling.common.network.packet;

import io.korti.bettermuffling.client.gui.GuiHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenScreenPacket  {

    private final BlockPos pos;

    public OpenScreenPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(final OpenScreenPacket msg, final PacketBuffer buf)  {
        buf.writeBlockPos(msg.pos);
    }

    public static OpenScreenPacket decode(final PacketBuffer buf) {
        return new OpenScreenPacket(buf.readBlockPos());
    }

    public static class Handler {
        public static void handle(final OpenScreenPacket msg, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                GuiHandler.openMufflingGui(msg.pos);
            });

            ctx.get().setPacketHandled(true);
        }
    }

}
