package net.korti.bettermuffling.common.network.packet;

import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class RequestMufflingUpdatePacket {

    private final String dataHash;
    private final BlockPos pos;

    public RequestMufflingUpdatePacket(String dataHash, BlockPos pos) {
        this.dataHash = dataHash;
        this.pos = pos;
    }

    public String getDataHash() {
        return dataHash;
    }

    public static void encode(RequestMufflingUpdatePacket packet, PacketBuffer buf) {
        buf.writeString(packet.dataHash);
        buf.writeBlockPos(packet.pos);
    }

    public static RequestMufflingUpdatePacket decode(PacketBuffer buf) {
        return new RequestMufflingUpdatePacket(buf.readString(32767), buf.readBlockPos());
    }

    public static class Handler {
        public static void handle(final RequestMufflingUpdatePacket packet, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                final ServerPlayerEntity player = ctx.get().getSender();
                final World world = Objects.requireNonNull(player).getServerWorld();
                final TileEntity te = world.getTileEntity(packet.pos);
                if(te instanceof TileMuffling) {
                    ((TileMuffling) te).syncToClient(packet, player);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
