package net.korti.bettermuffling.common.block;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.client.util.MufflingCache;
import net.korti.bettermuffling.common.network.PacketHandler;
import net.korti.bettermuffling.common.network.packet.MufflingAreaEventPacket;
import net.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import net.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameter;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class MufflingBlock extends ContainerBlock {

    public MufflingBlock() {
        super(Properties.create(Material.WOOL).sound(SoundType.CLOTH).noDrops());
        this.setRegistryName(BetterMuffling.MOD_ID, "muffling_block");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileMuffling();
    }

    @Override
    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
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
                final ItemStack stack = new ItemStack(this);
                final CompoundNBT tileData = tileMuffling.writeMufflingData(new CompoundNBT());
                stack.setTagInfo("tileData", tileData);

                final ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                itemEntity.setDefaultPickupDelay();
                worldIn.addEntity(itemEntity);
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }
}
