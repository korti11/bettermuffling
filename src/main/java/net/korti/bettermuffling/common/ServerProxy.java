package net.korti.bettermuffling.common;

import net.korti.bettermuffling.client.ClientProxy;
import net.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;

public class ServerProxy {

    public void preInit() {

    }

    public Runnable getMufflingDataPacketRunnable(final MufflingDataPacket packet, final NetworkEvent.Context ctx) {
        return () -> {
            final ServerPlayerEntity player = ctx.getSender();
            final World world = Objects.requireNonNull(player).getServerWorld();
            final TileEntity te = world.getTileEntity(packet.getPos());
            if(te instanceof TileMuffling) {
                ((TileMuffling) te).readMufflingData(packet.getMufflingData());
                te.markDirty();
            }
        };
    }

    public boolean isShiftKeyDown() {
        return false;
    }
}
