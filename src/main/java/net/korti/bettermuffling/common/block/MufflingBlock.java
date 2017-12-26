package net.korti.bettermuffling.common.block;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.common.constant.ModInfo;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MufflingBlock extends BlockContainer {

    public MufflingBlock() {
        super(Material.CLOTH);
        setCreativeTab(BetterMuffling.creativeTab);
        setRegistryName(ModInfo.MOD_ID, "muffling_block");
        setUnlocalizedName(ModInfo.MOD_ID + "." + "muffling_block");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileMuffling();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
