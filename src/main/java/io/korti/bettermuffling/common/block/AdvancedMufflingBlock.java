package io.korti.bettermuffling.common.block;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class AdvancedMufflingBlock extends MufflingBlock {

    @Override
    public void setPlacedBy(Level worldIn, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity player, ItemStack itemStack) {
        super.setPlacedBy(worldIn, blockPos, blockState, player, itemStack);
        final BlockEntity te = worldIn.getBlockEntity(blockPos);
        if(te instanceof TileMuffling) {
            ((TileMuffling) te).setAdvancedMode(true);
        }
    }
}
