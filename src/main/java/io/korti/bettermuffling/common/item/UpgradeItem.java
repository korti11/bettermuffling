package io.korti.bettermuffling.common.item;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class UpgradeItem extends Item {

    public UpgradeItem() {
        super(new Properties().group(ItemGroup.MISC));
        setRegistryName(BetterMuffling.MOD_ID, "upgrade");
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        final PlayerEntity player = context.getPlayer();
        final BlockState newBlockState = BetterMufflingBlocks.advancedMufflingBlock.getDefaultState();

        if(!world.isRemote && player != null
                && world.getBlockState(pos).getBlock().equals(BetterMufflingBlocks.mufflingBlock)) {
            final TileEntity oldTe = world.getTileEntity(pos);
            if(oldTe instanceof TileMuffling) {
                // Set new block without update the client.
                world.setBlockState(pos, newBlockState, 0);

                // Get the new tile entity and copy the data from the old one to the new one
                final TileMuffling newTe = (TileMuffling) world.getTileEntity(pos);
                final CompoundNBT teData = new CompoundNBT();
                oldTe.write(teData);
                newTe.read(teData);
                newTe.setAdvancedMode(true);

                // Notify the world of the block update
                world.notifyBlockUpdate(pos, newBlockState, newBlockState, 3);

                // Reduce the upgrade stack by one
                player.getHeldItem(context.getHand()).grow(-1);
            }
        }

        return super.onItemUse(context);
    }
}
