package io.korti.bettermuffling.common.block;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.network.PacketHandler;
import io.korti.bettermuffling.common.network.packet.MufflingAreaEventPacket;
import io.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class MufflingBlock extends Block {

    public MufflingBlock() {
        super(Properties.create(Material.WOOL).sound(SoundType.CLOTH).noDrops());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        final CompoundNBT mufflingData = stack.getChildTag("tileData");
        if(mufflingData != null && BetterMufflingConfig.CLIENT.tooltipEnable.get()) {
            if (BetterMuffling.proxy.isShiftKeyDown()) {
                if(mufflingData.contains("placerName")) {
                    tooltip.add(new StringTextComponent("Owner: " +
                            TextFormatting.GRAY + mufflingData.getString("placerName") + TextFormatting.RESET));
                }
                tooltip.add(new StringTextComponent(
                        I18n.format("button.muffling_block.range") + ": " +
                                TextFormatting.GRAY + mufflingData.getShort("range") + TextFormatting.RESET));
                Arrays.stream(SoundCategory.values())
                        .filter(category -> category != SoundCategory.MASTER && category != SoundCategory.MUSIC)
                        .forEach(category -> {
                            final String categoryName =
                                    I18n.format("soundCategory." + category.getName()) + ": ";
                            final float value = mufflingData.getFloat(category.getName());
                            final String categoryValue = value == 0.0F ? I18n.format("options.off") :
                                    (int)(value * 100) + "%";
                            tooltip.add(new StringTextComponent(categoryName +
                                    TextFormatting.GRAY + categoryValue + TextFormatting.RESET));
                        });
            } else {
                tooltip.add(new TranslationTextComponent("tooltip.hold_key.info",
                        TextFormatting.UNDERLINE + "Shift" + TextFormatting.RESET));
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileMuffling();
    }

    @Override
    public boolean onBlockActivated(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
                                    BlockRayTraceResult traceResult) {
        if(!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileMuffling && !player.isSneaking() && ((TileMuffling) te).canAccess(player)) {
                PacketHandler.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new OpenScreenPacket(pos));
                return true;
            }
        }
        return super.onBlockActivated(blockState, worldIn, pos, player, hand, traceResult);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos blockPos, BlockState blockState,
                                @Nullable LivingEntity player, ItemStack itemStack) {
        if(player != null) {
            final TileEntity te = worldIn.getTileEntity(blockPos);
            if(te instanceof TileMuffling) {
                ((TileMuffling) te).setPlacer(player.getUniqueID());
                final CompoundNBT tileData = itemStack.getChildTag("tileData");
                if (tileData != null) {
                    ((TileMuffling) te).readMufflingData(tileData);
                }
            }
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                   boolean willHarvest, IFluidState fluid) {
        if(!world.isRemote) {
            PacketHandler.send(PacketDistributor.ALL.noArg(), MufflingAreaEventPacket.PLAYER_LEFT);
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        final TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileMuffling) {
            final TileMuffling tileMuffling = (TileMuffling) te;
            if(worldIn.isRemote) {
                MufflingCache.removeMufflingPos(pos);
            }
            if(!worldIn.isRemote && !player.isCreative()) {
                final ItemStack stack = getStackWithTileData(tileMuffling, true);
                final ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                itemEntity.setDefaultPickupDelay();
                worldIn.addEntity(itemEntity);
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        final TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileMuffling && world instanceof World && player.isCreative()) {
            return getStackWithTileData((TileMuffling) te, false);
        }
        return new ItemStack(this);
    }

    private ItemStack getStackWithTileData(TileMuffling tileMuffling, boolean writePlayerName) {
        final ItemStack stack = new ItemStack(this);
        final CompoundNBT tileData = tileMuffling.writeMufflingData(new CompoundNBT(), writePlayerName);
        stack.setTagInfo("tileData", tileData);
        return stack;
    }
}
