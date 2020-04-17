package io.korti.bettermuffling.common;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;

public class ServerProxy {

    public void preInit() {

    }

    public Runnable getMufflingDataPacketRunnable(final MufflingDataPacket packet, final NetworkEvent.Context ctx) {
        return () -> {
            BetterMuffling.LOG.debug("Received muffling data from the client.");
            final ServerPlayerEntity player = ctx.getSender();
            final World world = Objects.requireNonNull(player).getEntityWorld();
            final TileEntity te = world.getTileEntity(packet.getPos());
            if(te instanceof TileMuffling) {
                ((TileMuffling) te).readMufflingData(packet.getMufflingData());
                te.markDirty();
                ((TileMuffling) te).syncToAllClients();
            }
        };
    }

    public boolean isShiftKeyDown() {
        return false;
    }
}
