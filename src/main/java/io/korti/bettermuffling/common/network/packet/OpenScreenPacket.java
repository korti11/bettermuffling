package io.korti.bettermuffling.common.network.packet;

import io.korti.bettermuffling.client.gui.GuiHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenScreenPacket(BlockPos pos) {

    public static void encode(final OpenScreenPacket msg, final FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static OpenScreenPacket decode(final FriendlyByteBuf buf) {
        return new OpenScreenPacket(buf.readBlockPos());
    }

    public static class Handler {
        public static void handle(final OpenScreenPacket msg, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> GuiHandler.openMufflingGui(msg.pos));
            ctx.get().setPacketHandled(true);
        }
    }

}
