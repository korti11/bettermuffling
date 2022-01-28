package io.korti.bettermuffling.common.network.packet;

import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MufflingDataPacket {

    private final BlockPos pos;
    private final CompoundTag mufflingData;

    public MufflingDataPacket(BlockPos pos, CompoundTag mufflingData) {
        this.pos = pos;
        this.mufflingData = mufflingData;
    }

    public static void encode(final MufflingDataPacket packet, final FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeNbt(packet.mufflingData);
    }

    public static MufflingDataPacket decode(final FriendlyByteBuf buf) {
        return new MufflingDataPacket(buf.readBlockPos(), buf.readNbt());
    }

    public BlockPos getPos() {
        return pos;
    }

    public CompoundTag getMufflingData() {
        return mufflingData;
    }

    public static class Handler {

        public static void handle(final MufflingDataPacket packet, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(BetterMuffling.proxy.getMufflingDataPacketRunnable(packet, ctx.get()));
            ctx.get().setPacketHandled(true);
        }

    }
}
