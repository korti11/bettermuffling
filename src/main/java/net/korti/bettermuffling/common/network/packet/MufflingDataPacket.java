package net.korti.bettermuffling.common.network.packet;

import net.korti.bettermuffling.client.ClientProxy;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
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

    public static class Handler {

        public static void handle(final MufflingDataPacket packet, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                final LogicalSide side = ctx.get().getDirection().getReceptionSide();
                final World world;
                final TileEntity te;
                if(side == LogicalSide.SERVER) {
                    final ServerPlayerEntity player = ctx.get().getSender();
                    world = Objects.requireNonNull(player).getServerWorld();
                } else {
                    world = ClientProxy.getWorld();
                }
                te = world.getTileEntity(packet.pos);
                if(te instanceof TileMuffling) {
                    ((TileMuffling) te).readMufflingData(packet.mufflingData);
                    te.markDirty();
                }
            });
            ctx.get().setPacketHandled(true);
        }

    }
}
