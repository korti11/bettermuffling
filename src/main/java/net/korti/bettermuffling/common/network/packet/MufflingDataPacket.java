package net.korti.bettermuffling.common.network.packet;

import net.korti.bettermuffling.BetterMuffling;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MufflingDataPacket {

    private final BlockPos pos;
    private final CompoundNBT mufflingData;

    public MufflingDataPacket(BlockPos pos, CompoundNBT mufflingData) {
        this.pos = pos;
        this.mufflingData = mufflingData;
    }

    public static void encode(final MufflingDataPacket packet, final PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeCompoundTag(packet.mufflingData);
    }

    public static MufflingDataPacket decode(final PacketBuffer buf) {
        return new MufflingDataPacket(buf.readBlockPos(), buf.readCompoundTag());
    }

    public BlockPos getPos() {
        return pos;
    }

    public CompoundNBT getMufflingData() {
        return mufflingData;
    }

    public static class Handler {

        public static void handle(final MufflingDataPacket packet, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(BetterMuffling.proxy.getMufflingDataPacketRunnable(packet, ctx.get()));
            ctx.get().setPacketHandled(true);
        }

    }
}
