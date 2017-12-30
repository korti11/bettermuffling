package net.korti.bettermuffling.common.network;

import io.netty.buffer.ByteBuf;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateTileEntityRequestMessage implements IMessage {

    private BlockPos pos;

    public UpdateTileEntityRequestMessage() {
    }

    public UpdateTileEntityRequestMessage(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.pos = buffer.readBlockPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeBlockPos(pos);
    }

    public static class MessageHandler implements IMessageHandler<UpdateTileEntityRequestMessage, IMessage> {

        @Override
        public IMessage onMessage(UpdateTileEntityRequestMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                final World world = ctx.getServerHandler().player.world;
                final TileEntity tile = world.getTileEntity(message.pos);
                if (tile instanceof TileMuffling) {
                    ((TileMuffling) tile).syncToClient();
                }
            }
            return null;
        }
    }
}
