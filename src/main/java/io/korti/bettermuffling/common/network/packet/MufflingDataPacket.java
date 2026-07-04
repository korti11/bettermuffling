package io.korti.bettermuffling.common.network.packet;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MufflingDataPacket(BlockPos pos, CompoundTag mufflingData) implements CustomPacketPayload {

    public static final Type<MufflingDataPacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(BetterMuffling.MOD_ID, "muffling_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MufflingDataPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, MufflingDataPacket::pos,
                    ByteBufCodecs.COMPOUND_TAG, MufflingDataPacket::mufflingData,
                    MufflingDataPacket::new
            );

    @Override
    public Type<MufflingDataPacket> type() {
        return TYPE;
    }

    public static void handleServer(MufflingDataPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BetterMuffling.LOG.debug("Received muffling data from the client.");
            ServerPlayer player = (ServerPlayer) ctx.player();
            BlockEntity te = player.level().getBlockEntity(packet.pos());
            if (te instanceof MufflingBlockEntity mbe) {
                mbe.readMufflingData(packet.mufflingData());
                te.setChanged();
                mbe.syncToAllClients();
            }
        });
    }

    public static void handleClient(MufflingDataPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BetterMuffling.LOG.debug("Received muffling data from server.");
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            BlockEntity te = level.getBlockEntity(packet.pos());
            if (te instanceof MufflingBlockEntity mbe) {
                mbe.readMufflingData(packet.mufflingData());
                te.setChanged();
            }
        });
    }
}
