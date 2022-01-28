package io.korti.bettermuffling.common;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class ServerProxy {

    public void preInit() {

    }

    public Runnable getMufflingDataPacketRunnable(final MufflingDataPacket packet, final NetworkEvent.Context ctx) {
        return () -> {
            BetterMuffling.LOG.debug("Received muffling data from the client.");
            final ServerPlayer player = ctx.getSender();
            final Level world = Objects.requireNonNull(player).getCommandSenderWorld();
            final BlockEntity te = world.getBlockEntity(packet.getPos());
            if(te instanceof TileMuffling) {
                ((TileMuffling) te).readMufflingData(packet.getMufflingData());
                te.setChanged();
                ((TileMuffling) te).syncToAllClients();
            }
        };
    }

    public boolean isShiftKeyDown() {
        return false;
    }
}
