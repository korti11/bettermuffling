package net.korti.bettermuffling.common.block;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.common.network.PacketHandler;
import net.korti.bettermuffling.common.network.packet.OpenScreenPacket;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class MufflingBlock extends ContainerBlock {

    public MufflingBlock() {
        super(Properties.create(Material.WOOL).sound(SoundType.CLOTH));
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
            if(te instanceof TileMuffling && ((TileMuffling) te).canAccess(player)) {
                PacketHandler.sendToPlayer(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new OpenScreenPacket(pos));
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
            }
        }
    }
}
