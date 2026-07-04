package io.korti.bettermuffling.common.network.packet;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestMufflingUpdatePacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<RequestMufflingUpdatePacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(BetterMuffling.MOD_ID, "request_muffling_update"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestMufflingUpdatePacket> STREAM_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, RequestMufflingUpdatePacket::pos, RequestMufflingUpdatePacket::new);

    @Override
    public Type<RequestMufflingUpdatePacket> type() {
        return TYPE;
    }

    public static void handle(RequestMufflingUpdatePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            BlockEntity te = player.level().getBlockEntity(packet.pos());
            if (te instanceof MufflingBlockEntity mbe) {
                mbe.syncToClient(player);
            }
        });
    }
}
