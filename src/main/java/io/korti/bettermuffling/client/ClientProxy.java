package io.korti.bettermuffling.client;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.ServerProxy;
import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class ClientProxy extends ServerProxy {

    public static ClientWorld getWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public Runnable getMufflingDataPacketRunnable(MufflingDataPacket packet, NetworkEvent.Context ctx) {
        return () -> {
            BetterMuffling.LOG.debug("Received muffling data from server.");
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

    @Override
    public boolean isShiftKeyDown() {
        long handler = Minecraft.getInstance().mainWindow.getHandle();
        return InputMappings.isKeyDown(handler, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputMappings.isKeyDown(handler, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
}
