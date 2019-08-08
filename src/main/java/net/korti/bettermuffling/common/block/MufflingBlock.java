package net.korti.bettermuffling.common.block;

import net.korti.bettermuffling.BetterMuffling;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MufflingBlock extends ContainerBlock {

    public MufflingBlock() {
        super(Properties.create(Material.WOOL).sound(SoundType.CLOTH));
        this.setRegistryName(BetterMuffling.MOD_ID, "muffling_block");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }
}
