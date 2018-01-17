package net.korti.bettermuffling.common.block;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.common.constant.ModInfo;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.korti.bettermuffling.common.util.TileCache;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class MufflingBlock extends BlockContainer {

    public MufflingBlock() {
        super(Material.CLOTH);
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(ModInfo.MOD_ID, "muffling_block");
        setUnlocalizedName(ModInfo.MOD_ID + "." + "muffling_block");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileMuffling();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            player.openGui(BetterMuffling.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null) {
                if(tile instanceof TileMuffling) {
                    ((TileMuffling) tile).hideIndicator();
                }
                MinecraftForge.EVENT_BUS.unregister(tile);
                TileCache.removeTileEntity(tile);
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
}
