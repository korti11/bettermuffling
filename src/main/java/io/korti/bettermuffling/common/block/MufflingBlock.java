package io.korti.bettermuffling.common.block;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.network.PacketHandler;
import io.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class MufflingBlock extends Block implements EntityBlock {

    public MufflingBlock() {
        super(Properties.of(Material.WOOL).sound(SoundType.WOOL).noDrops());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip,
                                @Nonnull TooltipFlag flagIn) {
        final CompoundTag mufflingData = stack.getTagElement("tileData");
        if (mufflingData != null && BetterMufflingConfig.CLIENT.tooltipEnable.get()) {
            if (BetterMuffling.proxy.isShiftKeyDown()) {
                if (mufflingData.contains("placerName")) {
                    tooltip.add(new TextComponent("Owner: " +
                            ChatFormatting.GRAY + mufflingData.getString("placerName") + ChatFormatting.RESET));
                }
                tooltip.add(new TextComponent(
                        I18n.get("button.muffling_block.range") + ": " +
                                ChatFormatting.GRAY + mufflingData.getShort("range") + ChatFormatting.RESET));
                Arrays.stream(SoundSource.values())
                        .filter(category -> category != SoundSource.MASTER && category != SoundSource.MUSIC)
                        .forEach(category -> {
                            final String categoryName =
                                    I18n.get("soundCategory." + category.getName()) + ": ";
                            final float value = mufflingData.getFloat(category.getName());
                            final String categoryValue = value == 0.0F ? I18n.get("options.off") :
                                    (int) (value * 100) + "%";
                            tooltip.add(new TextComponent(categoryName +
                                    ChatFormatting.GRAY + categoryValue + ChatFormatting.RESET));
                        });
            } else {
                tooltip.add(new TranslatableComponent("tooltip.hold_key.info",
                        ChatFormatting.UNDERLINE + "Shift" + ChatFormatting.RESET));
            }
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public InteractionResult use(@Nonnull BlockState blockState, Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player,
                                 @Nonnull InteractionHand hand, @Nonnull BlockHitResult traceResult) {
        if (!worldIn.isClientSide) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof MufflingBlockEntity && !player.isCrouching() && ((MufflingBlockEntity) te).canAccess(player)) {
                PacketHandler.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new OpenScreenPacket(pos));
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
            if (te instanceof MufflingBlockEntity) {
                ((MufflingBlockEntity) te).setPlacer(player.getUUID());
                final CompoundTag tileData = itemStack.getTagElement("tileData");
                if (tileData != null) {
                    ((MufflingBlockEntity) te).readMufflingData(tileData);
                }
            }
        }
    }

    @Override
    public void playerWillDestroy(Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        final BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof final MufflingBlockEntity mufflingBlockEntity) {
            if (worldIn.isClientSide) {
                MufflingCache.removeMufflingPos(pos);
            }
            if (!worldIn.isClientSide && !player.isCreative()) {
                final ItemStack stack = getStackWithTileData(mufflingBlockEntity, true);
                final ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                itemEntity.setDefaultPickUpDelay();
                worldIn.addFreshEntity(itemEntity);
            }
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        final BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof MufflingBlockEntity && world instanceof Level && player.isCreative()) {
            return getStackWithTileData((MufflingBlockEntity) te, false);
        }
        return new ItemStack(this);
    }

    private ItemStack getStackWithTileData(MufflingBlockEntity tileMuffling, boolean writePlayerName) {
        final ItemStack stack = new ItemStack(this);
        final CompoundTag tileData = tileMuffling.writeMufflingData(new CompoundTag(), writePlayerName);
        stack.addTagElement("tileData", tileData);
        return stack;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState blockState) {
        return new MufflingBlockEntity(pos, blockState);
    }
}
