package io.korti.bettermuffling.client;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.ServerProxy;
import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class ClientProxy extends ServerProxy {

    public static ClientLevel getWorld() {
        return Minecraft.getInstance().level;
    }

    @Override
    public Runnable getMufflingDataPacketRunnable(MufflingDataPacket packet, NetworkEvent.Context ctx) {
        return () -> {
            BetterMuffling.LOG.debug("Received muffling data from server.");
            final LogicalSide side = ctx.getDirection().getReceptionSide();
            final Level world;
            final BlockEntity te;
            boolean lanWorld = false;
            if(side == LogicalSide.SERVER) {
                final ServerPlayer player = ctx.getSender();
                world = Objects.requireNonNull(player).getCommandSenderWorld();
                lanWorld = Objects.requireNonNull(world.getServer()).isPublished();
            } else {
                world = ClientProxy.getWorld();
            }
            te = world.getBlockEntity(packet.getPos());
            if(te instanceof TileMuffling) {
                ((TileMuffling) te).readMufflingData(packet.getMufflingData());
                te.setChanged();
                if (lanWorld) {
                    ((TileMuffling) te).syncToAllClients();
                }
            }
        };
    }

    @Override
    public boolean isShiftKeyDown() {
        long handler = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handler, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputConstants.isKeyDown(handler, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
}
