package net.korti.bettermuffling.client;

import net.korti.bettermuffling.common.ServerProxy;
import net.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;

public class ClientProxy extends ServerProxy {

    public static ClientWorld getWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public Runnable getMufflingDataPacketRunnable(MufflingDataPacket packet, NetworkEvent.Context ctx) {
        return () -> {
            final LogicalSide side = ctx.getDirection().getReceptionSide();
            final World world;
            final TileEntity te;
            if(side == LogicalSide.SERVER) {
                final ServerPlayerEntity player = ctx.getSender();
                world = Objects.requireNonNull(player).getServerWorld();
            } else {
                world = ClientProxy.getWorld();
            }
            te = world.getTileEntity(packet.getPos());
            if(te instanceof TileMuffling) {
                ((TileMuffling) te).readMufflingData(packet.getMufflingData());
                te.markDirty();
            }
        };
    }
}
