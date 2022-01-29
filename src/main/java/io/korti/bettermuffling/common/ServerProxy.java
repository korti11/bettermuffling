package io.korti.bettermuffling.common;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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
            if (te instanceof MufflingBlockEntity) {
                ((MufflingBlockEntity) te).readMufflingData(packet.getMufflingData());
                te.setChanged();
                ((MufflingBlockEntity) te).syncToAllClients();
            }
        };
    }

    public boolean isShiftKeyDown() {
        return false;
    }
}
