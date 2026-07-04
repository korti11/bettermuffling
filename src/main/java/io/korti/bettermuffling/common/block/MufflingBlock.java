package io.korti.bettermuffling.common.block;

import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import io.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MufflingBlock extends Block implements EntityBlock {

    public MufflingBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public InteractionResult useWithoutItem(@Nonnull BlockState blockState, Level worldIn, @Nonnull BlockPos pos,
                                            @Nonnull Player player, @Nonnull BlockHitResult traceResult) {
        if (!worldIn.isClientSide()) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof MufflingBlockEntity && !player.isCrouching() && ((MufflingBlockEntity) te).canAccess(player)) {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new OpenScreenPacket(pos));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(@Nonnull Level worldIn, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState,
                            @Nullable LivingEntity player, @Nonnull ItemStack itemStack) {
        if (player != null) {
            final BlockEntity te = worldIn.getBlockEntity(blockPos);
            if (te instanceof MufflingBlockEntity mbe) {
                mbe.setPlacer(player.getUUID());
                CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
                if (customData != null) {
                    CompoundTag tag = customData.copyTag();
                    tag.getCompound("tileData").ifPresent(mbe::readMufflingData);
                }
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        final BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof final MufflingBlockEntity mufflingBlockEntity) {
            if (worldIn.isClientSide()) {
                MufflingCache.removeMufflingPos(pos);
            }
            if (!worldIn.isClientSide() && !player.isCreative()) {
                final ItemStack stack = getStackWithTileData(mufflingBlockEntity, true);
                final ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                itemEntity.setDefaultPickUpDelay();
                worldIn.addFreshEntity(itemEntity);
            }
        }
        return super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player player) {
        final BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof MufflingBlockEntity && world instanceof Level && player.isCreative()) {
            return getStackWithTileData((MufflingBlockEntity) te, false);
        }
        return new ItemStack(this);
    }

    ItemStack getStackWithTileData(MufflingBlockEntity tileMuffling, boolean writePlayerName) {
        final ItemStack stack = new ItemStack(this);
        final CompoundTag tileData = tileMuffling.writeMufflingData(new CompoundTag(), writePlayerName);
        CompoundTag wrapper = new CompoundTag();
        wrapper.put("tileData", tileData);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(wrapper));
        return stack;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState blockState) {
        return new MufflingBlockEntity(pos, blockState);
    }
}
