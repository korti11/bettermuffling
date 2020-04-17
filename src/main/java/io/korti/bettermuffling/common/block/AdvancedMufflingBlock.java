package io.korti.bettermuffling.common.block;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AdvancedMufflingBlock extends MufflingBlock {

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity player, ItemStack itemStack) {
        super.onBlockPlacedBy(worldIn, blockPos, blockState, player, itemStack);
        final TileEntity te = worldIn.getTileEntity(blockPos);
        if(te instanceof TileMuffling) {
            ((TileMuffling) te).setAdvancedMode(true);
        }
    }
}
