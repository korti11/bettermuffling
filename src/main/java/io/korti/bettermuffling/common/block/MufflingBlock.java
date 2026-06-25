package io.korti.bettermuffling.common.block;

import com.mojang.blaze3d.platform.InputConstants;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MufflingBlock extends Block implements EntityBlock {

    public MufflingBlock() {
        super(Properties.of().sound(SoundType.WOOL).noLootTable());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext context, @Nonnull List<Component> tooltip,
                                @Nonnull TooltipFlag flagIn) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;
        CompoundTag tag = customData.copyTag();
        CompoundTag mufflingData = tag.contains("tileData") ? tag.getCompound("tileData") : null;
        if (mufflingData != null && BetterMufflingConfig.CLIENT.tooltipEnable.get()) {
            long handle = Minecraft.getInstance().getWindow().getWindow();
            boolean shift = InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                    InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
            if (shift) {
                if (mufflingData.contains("placerName")) {
                    tooltip.add(Component.literal("Owner: " +
                            ChatFormatting.GRAY + mufflingData.getString("placerName") + ChatFormatting.RESET));
                }
                tooltip.add(Component.literal(
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
                            tooltip.add(Component.literal(categoryName +
                                    ChatFormatting.GRAY + categoryValue + ChatFormatting.RESET));
                        });
            } else {
                tooltip.add(Component.translatable("tooltip.hold_key.info",
                        ChatFormatting.UNDERLINE + "Shift" + ChatFormatting.RESET));
            }
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public InteractionResult useWithoutItem(@Nonnull BlockState blockState, Level worldIn, @Nonnull BlockPos pos,
                                            @Nonnull Player player, @Nonnull BlockHitResult traceResult) {
        if (!worldIn.isClientSide) {
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
                    if (tag.contains("tileData")) {
                        mbe.readMufflingData(tag.getCompound("tileData"));
                    }
                }
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
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
        return super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader world, BlockPos pos, Player player) {
        final BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof MufflingBlockEntity && world instanceof Level && player.isCreative()) {
            return getStackWithTileData((MufflingBlockEntity) te, false);
        }
        return new ItemStack(this);
    }

    private ItemStack getStackWithTileData(MufflingBlockEntity tileMuffling, boolean writePlayerName) {
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
