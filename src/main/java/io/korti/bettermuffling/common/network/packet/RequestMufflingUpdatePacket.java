package io.korti.bettermuffling.common.network.packet;

import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record RequestMufflingUpdatePacket(BlockPos pos) {


    public static void encode(RequestMufflingUpdatePacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
    }

    public static RequestMufflingUpdatePacket decode(FriendlyByteBuf buf) {
        return new RequestMufflingUpdatePacket(buf.readBlockPos());
    }

    public static class Handler {
        public static void handle(final RequestMufflingUpdatePacket packet, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                final ServerPlayer player = ctx.get().getSender();
                final Level world = Objects.requireNonNull(player).getCommandSenderWorld();
                final BlockEntity te = world.getBlockEntity(packet.pos);
                if (te instanceof MufflingBlockEntity) {
                    ((MufflingBlockEntity) te).syncToClient(player);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
