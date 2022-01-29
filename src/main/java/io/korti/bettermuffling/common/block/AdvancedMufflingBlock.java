package io.korti.bettermuffling.common.block;

import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class AdvancedMufflingBlock extends MufflingBlock {

    @Override
    public void setPlacedBy(@NotNull Level worldIn, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable LivingEntity player, @NotNull ItemStack itemStack) {
        super.setPlacedBy(worldIn, blockPos, blockState, player, itemStack);
        final BlockEntity te = worldIn.getBlockEntity(blockPos);
        if (te instanceof MufflingBlockEntity) {
            ((MufflingBlockEntity) te).setAdvancedMode(true);
        }
    }
}
