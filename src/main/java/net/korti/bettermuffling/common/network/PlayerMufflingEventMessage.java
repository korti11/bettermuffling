package net.korti.bettermuffling.common.network;

import io.netty.buffer.ByteBuf;
import net.korti.bettermuffling.client.event.MufflingAreaEvent;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerMufflingEventMessage implements IMessage {

    private boolean entered;

    public PlayerMufflingEventMessage() {
    }

    public PlayerMufflingEventMessage(boolean entered) {
        this.entered = entered;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            PacketBuffer buffer = new PacketBuffer(buf);
            this.entered = buffer.getBoolean(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeBoolean(this.entered);
    }

    public static class MessageHandler implements IMessageHandler<PlayerMufflingEventMessage, IMessage> {
        @Override
        public IMessage onMessage(PlayerMufflingEventMessage message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                if(message.entered) {
                    MinecraftForge.EVENT_BUS.post(new MufflingAreaEvent.PlayerEntered());
                } else {
                    MinecraftForge.EVENT_BUS.post(new MufflingAreaEvent.PlayerLeft());
                }
            }
            return null;
        }
    }
}
