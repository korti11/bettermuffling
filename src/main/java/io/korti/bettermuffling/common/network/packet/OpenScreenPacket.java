package io.korti.bettermuffling.common.network.packet;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.gui.GuiHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenScreenPacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<OpenScreenPacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(BetterMuffling.MOD_ID, "open_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenScreenPacket> STREAM_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, OpenScreenPacket::pos, OpenScreenPacket::new);

    @Override
    public Type<OpenScreenPacket> type() {
        return TYPE;
    }

    public static void handle(OpenScreenPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> GuiHandler.openMufflingGui(packet.pos()));
    }
}
