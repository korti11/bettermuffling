package io.korti.bettermuffling.common.item;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class UpgradeItem extends Item {

    public UpgradeItem() {
        super(new Properties().group(ItemGroup.MISC));
        setRegistryName(BetterMuffling.MOD_ID, "upgrade");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        final String[] lines = I18n.format("tooltip.upgrade.info").split("\n");
        Arrays.stream(lines).forEach(l -> tooltip.add(new StringTextComponent(l)));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        final PlayerEntity player = context.getPlayer();
        final BlockState oldBlockState = world.getBlockState(pos);
        final BlockState newBlockState = BetterMufflingBlocks.advancedMufflingBlock.getDefaultState();

        if(!world.isRemote && player != null
                && oldBlockState.getBlock().equals(BetterMufflingBlocks.mufflingBlock)) {
            final TileEntity oldTe = world.getTileEntity(pos);
            if(oldTe instanceof TileMuffling) {
                // Set new block without update the client.
                world.setBlockState(pos, newBlockState, 0);

                // Get the new tile entity and copy the data from the old one to the new one
                final TileMuffling newTe = (TileMuffling) world.getTileEntity(pos);
                final CompoundNBT teData = new CompoundNBT();
                oldTe.write(teData);
                newTe.func_230337_a_(oldBlockState, teData);
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
