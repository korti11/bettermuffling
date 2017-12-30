package net.korti.bettermuffling.common.network;

import io.netty.buffer.ByteBuf;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class UpdateTileEntityMessage implements IMessage {

    private BlockPos pos;
    private NBTTagCompound compound;

    public UpdateTileEntityMessage() {

    }

    public UpdateTileEntityMessage(BlockPos pos, NBTTagCompound compound) {
        this.pos = pos;
        this.compound = compound;
    }

    public NBTTagCompound getCompound() {
        return compound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            PacketBuffer buffer = new PacketBuffer(buf);
            this.pos = buffer.readBlockPos();
            this.compound = buffer.readCompoundTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeBlockPos(pos);
        buffer.writeCompoundTag(compound);
    }

    public static class MessageHandler implements IMessageHandler<UpdateTileEntityMessage, IMessage> {
        @Override
        public IMessage onMessage(UpdateTileEntityMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                final World world = ctx.getServerHandler().player.world;
                final TileEntity tile = world.getTileEntity(message.pos);
                if (tile instanceof TileMuffling) {
                    ((TileMuffling) tile).onDataPacket(message);
                }
            }
            return null;
        }
    }
}
